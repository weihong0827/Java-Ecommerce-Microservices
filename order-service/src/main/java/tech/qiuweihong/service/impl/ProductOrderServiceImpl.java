package tech.qiuweihong.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.component.PaymentFactory;
import tech.qiuweihong.config.RabbitMQConfig;
import tech.qiuweihong.enums.*;
import tech.qiuweihong.feign.CouponFeignService;
import tech.qiuweihong.feign.ProductFeignService;
import tech.qiuweihong.feign.UserFeignService;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.mapper.ProductOrderItemMapper;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.model.OrderMessage;
import tech.qiuweihong.model.ProductOrderDO;
import tech.qiuweihong.mapper.ProductOrderMapper;
import tech.qiuweihong.model.ProductOrderItemDO;
import tech.qiuweihong.request.LockCouponRecordRequest;
import tech.qiuweihong.request.LockProductRequest;
import tech.qiuweihong.request.OrderItemRequest;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.CouponRecordVO;
import tech.qiuweihong.vo.OrderItemVO;
import tech.qiuweihong.vo.ProductOrderAddressVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductOrderServiceImpl  implements ProductOrderService {
    @Autowired
    private ProductOrderMapper productOrderMapper;
    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private ProductOrderItemMapper productOrderItemMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private PaymentFactory paymentFactory;
    /**
     * Submits an order with the given order request.
     *
     * @param submitOrderRequest A SubmitOrderRequest object containing order information.
     * @return A JsonData object representing the order result.
     */
    @Override
    public JsonData submitOrder(SubmitOrderRequest submitOrderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String outTradeNo = CommonUtils.getStringNumRandom(32);
        ProductOrderAddressVO addressVO = this.getUserAddress(submitOrderRequest.getAddressId());
        log.info("address detail:{}",addressVO);

        List<Long> productIds = submitOrderRequest.getProductList();
        JsonData cartItemData = productFeignService.confirmOrderCartItem(productIds);
        List<OrderItemVO> orderItemVOS = cartItemData.getData(new TypeReference<>(){});
        if(orderItemVOS==null){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_CART_ITEM_NOT_EXIST);
        }
        this.checkPrice(orderItemVOS,submitOrderRequest);

        // Lock coupon records
        this.lockCouponRecords(submitOrderRequest,outTradeNo);
        
        // lock product stock
        this.lockProductStock(orderItemVOS,outTradeNo);

        // Create Order
        ProductOrderDO productOrderDO = this.saveProductOrder(submitOrderRequest,loginUser,outTradeNo,addressVO);

        // Create Order Items
        this.saveProductOrderItems(productOrderDO.getId(),outTradeNo,orderItemVOS);

        // Send delay message to close order when payment not successful
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOutTradeNo(outTradeNo);
        rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getOrderCloseDelayRoutingKey(),orderMessage);


        // Create Payment

        return null;
    }

    private void saveProductOrderItems(Long id, String outTradeNo, List<OrderItemVO> orderItemVOS) {
        List<ProductOrderItemDO> list = orderItemVOS.stream().map(orderItemVO->{
            ProductOrderItemDO productOrderItemDO = new ProductOrderItemDO();
            productOrderItemDO.setProductId(orderItemVO.getProductId());
            productOrderItemDO.setProductOrderId(id);
            productOrderItemDO.setProductImg(orderItemVO.getProductImg());
            productOrderItemDO.setProductName(orderItemVO.getProductTitle());
            productOrderItemDO.setCreateTime(new Date());
            productOrderItemDO.setOutTradeNo(outTradeNo);
            productOrderItemDO.setAmount(orderItemVO.getAmount());
            productOrderItemDO.setTotalAmount(orderItemVO.getTotalAmount());

            return productOrderItemDO;
        }).collect(Collectors.toList());
        productOrderItemMapper.insertBatch(list);
    }

    private ProductOrderDO saveProductOrder(SubmitOrderRequest submitOrderRequest, LoginUser loginUser, String outTradeNo, ProductOrderAddressVO addressVO) {
        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setUserId(loginUser.getId());
        productOrderDO.setNickname(loginUser.getName());

        productOrderDO.setOutTradeNo(outTradeNo);
        productOrderDO.setCreateTime(new Date());
        productOrderDO.setDel(0);
        productOrderDO.setOrderType(OrderType.NORMAL.name());

        productOrderDO.setPayAmount(submitOrderRequest.getActualAmount());
        productOrderDO.setTotalAmount(submitOrderRequest.getTotalAmount());
        productOrderDO.setState(OrderStatus.NEW.name());

        productOrderDO.setPayType(OrderPaymentType.valueOf(submitOrderRequest.getPayType()).name());


        productOrderDO.setReceiverAddress(JSON.toJSONString(addressVO));
        productOrderMapper.insert(productOrderDO);
        return productOrderDO;

    }

    private void lockProductStock(List<OrderItemVO> orderItemVOS, String outTradeNo) {
        LockProductRequest lockProductRequest = new LockProductRequest();
        List<OrderItemRequest> orderItemRequestList = orderItemVOS.stream().map(obj->{
            OrderItemRequest orderItemRequest = new OrderItemRequest();
            orderItemRequest.setProductId(obj.getProductId());
            orderItemRequest.setCount(obj.getBuyNum());
            return orderItemRequest;
        }).collect(Collectors.toList());
        lockProductRequest.setOrderItemList(orderItemRequestList);
        lockProductRequest.setOrderOutTradeNo(outTradeNo);
        JsonData data = productFeignService.lockProductStock(lockProductRequest);
        if (data.getCode()!=0){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_LOCK_PRODUCT_FAIL);
        }

    }


    private void lockCouponRecords(SubmitOrderRequest submitOrderRequest, String outTradeNo) {
        List<Long> lockCouponRecordIds = new ArrayList<>();
        if (submitOrderRequest.getCouponRecordId() > 0) {
            lockCouponRecordIds.add(submitOrderRequest.getCouponRecordId());
            LockCouponRecordRequest lockCouponRecordRequest = new LockCouponRecordRequest();
            lockCouponRecordRequest.setOrderOutTradeNo(outTradeNo);
            lockCouponRecordRequest.setLockCouponRecodIds(lockCouponRecordIds);
            JsonData data = couponFeignService.lockCouponRecord(lockCouponRecordRequest);
            if (data.getCode()!=0){
                throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
            }
        }
    }

    private void checkPrice(List<OrderItemVO> orderItemVOS, SubmitOrderRequest submitOrderRequest) {
        BigDecimal realPayAmount = new BigDecimal("0");
        if (orderItemVOS!=null){
            for(OrderItemVO orderItemVO :orderItemVOS){
                BigDecimal itemRealPayAmount = orderItemVO.getTotalAmount();
                realPayAmount = realPayAmount.add(itemRealPayAmount);
            }
        }
        CouponRecordVO couponRecordVO = getCartCouponRecord(submitOrderRequest.getCouponRecordId());

        if (couponRecordVO != null) {
            if (realPayAmount.compareTo(couponRecordVO.getConditionPrice())<0){
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_COUPON_FAIL);
            }
            if (couponRecordVO.getPrice().compareTo(realPayAmount)>0){
                realPayAmount = BigDecimal.ZERO;
            }else{
                realPayAmount = realPayAmount.subtract(couponRecordVO.getPrice());
            }
        }
        if (realPayAmount.compareTo(submitOrderRequest.getActualAmount())!=0){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
        }
    }

    private CouponRecordVO getCartCouponRecord(Long couponRecordId) {
        if (couponRecordId==null||couponRecordId<0){
            return null;
        }
        JsonData couponData = couponFeignService.findUserCouponRecordById(couponRecordId);
        if (couponData.getCode()!=0){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_COUPON_FAIL);
        }
        if (couponData.getCode()==0){
            CouponRecordVO couponRecordVO = couponData.getData(new TypeReference<>(){});
            if (!couponUsable(couponRecordVO)){
                throw new BizException(BizCodeEnum.COUPON_CONDITION_ERROR);
            }
            return couponRecordVO;
        }
        return null;

    }

    private boolean couponUsable(CouponRecordVO couponRecordVO) {
        if (couponRecordVO.getUseState().equalsIgnoreCase(CouponUseStateEnum.NEW.name())){
            long currTime = System.currentTimeMillis();
            long end = couponRecordVO.getEndTime().getTime();
            long start = couponRecordVO.getStartTime().getTime();
            if (currTime>=start&&currTime<=end){
                return true;
            }

        }
        return false;
    }

    private ProductOrderAddressVO getUserAddress(Long addressId) {
        JsonData data = userFeignService.getUserAddress(addressId);
        if (data.getCode()!= 0) {
            log.error("get address error");
            throw new BizException(BizCodeEnum.ADDRESS_NO_EXITS);
        }
        ProductOrderAddressVO addressVO = data.getData(new TypeReference<>(){});
        return addressVO;
    }

    @Override
    public String queryProductOrderState(String outTradeNo) {
        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>().eq("out_trade_no",outTradeNo));
        if (productOrderDO==null){
            return "";
        }else {
            return productOrderDO.getState();
        }

    }

    @Override
    public boolean closeProductOrder(OrderMessage orderMessage) {
        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>().eq("out_trade_no",orderMessage.getOutTradeNo()));
        if (productOrderDO == null) {
            return true;
        }

        if (productOrderDO.getState().equalsIgnoreCase(OrderStatus.PAID.name())){
            return true;
        }

        // Not paid
        // Query current payment state TODO
        boolean payResult = false;
        if (payResult){
            productOrderMapper.updateOrderPayState(productOrderDO.getOutTradeNo(),OrderStatus.PAID.name(),OrderStatus.NEW.name());
            return true;
        }
        productOrderMapper.updateOrderPayState(productOrderDO.getOutTradeNo(),OrderStatus.CANCELLED.name(),OrderStatus.NEW.name());


        return true;
    }

    @Override
    public JsonData handleOrderCallbackMsg(OrderPaymentType orderPaymentType, Map<String, String> paramMap) {

        if (orderPaymentType == OrderPaymentType.ALIPAY){
            String out_trade_no = paramMap.get("out_trade_no");
            String trade_status = paramMap.get("trade_status");
            if (trade_status.equalsIgnoreCase("TRADE_SUCCESS")){
                productOrderMapper.updateOrderPayState(out_trade_no,OrderStatus.PAID.name(),OrderStatus.NEW.name());
                return JsonData.buildSuccess();
            }
        }else if(orderPaymentType == OrderPaymentType.WECHAT) {
            // TODO Wechat pay
        }
        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_CALLBACK_NOT_SUCCESS);
    }
}
