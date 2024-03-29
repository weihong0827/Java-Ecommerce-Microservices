package tech.qiuweihong.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.transaction.annotation.Transactional;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.component.PaymentFactory;
import tech.qiuweihong.config.RabbitMQConfig;
import tech.qiuweihong.constants.CacheKey;
import tech.qiuweihong.constants.TimeConstant;
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
import tech.qiuweihong.request.*;
import tech.qiuweihong.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.*;

import java.math.BigDecimal;
import java.util.*;
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
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * Submits an order with the given order request.
     *
     * @param submitOrderRequest A SubmitOrderRequest object containing order information.
     * @return A JsonData object representing the order result.
     */
    @Override
    @Transactional
    public JsonData submitOrder(SubmitOrderRequest submitOrderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String orderToken = submitOrderRequest.getToken();
        if (StringUtil.isBlank(orderToken)){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_NOT_EXIST);
        }
        // Check token and delete token
        String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script,Long.class),
                Arrays.asList(String.format(CacheKey.OrderKey,loginUser.getId())));
        if (result==0L){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_EQUAL_FAIL);
        }


        String outTradeNo = CommonUtils.getStringNumRandom(32);
        ProductOrderAddressVO addressVO = this.getUserAddress(submitOrderRequest.getAddressId());
        log.info("address detail:{}",addressVO);

        List<Long> productIds = submitOrderRequest.getProductList();
        JsonData cartItemData = productFeignService.confirmOrderCartItem(productIds);
        log.info("Cart items retrieved:{}",cartItemData);
        List<OrderItemVO> orderItemVOS = cartItemData.getData(new TypeReference<>(){});
        log.info("Cart items retrieved:{}",orderItemVOS);
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
        PayInfoVO payInfoVO = new PayInfoVO(productOrderDO.getOutTradeNo(),productOrderDO.getPayAmount(),
                OrderPaymentType.valueOf(submitOrderRequest.getPayType().toUpperCase()),
                ClientType.valueOf(submitOrderRequest.getClientType().toUpperCase()),
                "order out trade no","", TimeConstant.ORDER_PAY_TIMEOUT_MILLS);
        String payResult = paymentFactory.pay(payInfoVO);
        if (StringUtil.isNotBlank(payResult)){
            return JsonData.buildSuccess(payResult);
        }else{
            return JsonData.buildResult(BizCodeEnum.PAY_ORDER_FAIL);
        }

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
            productOrderItemDO.setBuyNum(orderItemVO.getBuyNum());

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

        productOrderDO.setPayType(OrderPaymentType.valueOf(submitOrderRequest.getPayType().toUpperCase()).name());


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
                log.info("Item details:{}",orderItemVO);
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
            log.error("Price Check error, calculated price:{}, given price:{}",realPayAmount,submitOrderRequest.getActualAmount());
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
            // Order not exist
            return true;
        }

        if (productOrderDO.getState().equalsIgnoreCase(OrderStatus.PAID.name())){
            // already paid
            return true;
        }

        // Not paid
        // Query current payment state
        PayInfoVO payInfoVO = new PayInfoVO();
        payInfoVO.setPaymentType(OrderPaymentType.valueOf(productOrderDO.getPayType()));
        payInfoVO.setOutTradeNo(orderMessage.getOutTradeNo());
        String payResult = paymentFactory.queryPaymentStatus(payInfoVO);


        if (!payResult.isBlank()){
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

    /**
     * Retrieves the detailed information of product orders for a specific user.
     *
     * @param page the page number of the result set
     * @param size the number of records per page
     * @param state the state of the product orders to filter by (optional)
     *
     * @return a map containing the details of the product orders
     */
    @Override
    public Map<String, Object> detail(int page, int size, String state) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Page<ProductOrderDO> pageInfo = new Page<>(page,size);
        IPage<ProductOrderDO> orderDBPage = null;
        if (StringUtil.isNotBlank(state)){
            orderDBPage = productOrderMapper.selectPage(pageInfo,new QueryWrapper<ProductOrderDO>().eq("user_id",loginUser.getId()).eq("state",state));
        }else{
            orderDBPage = productOrderMapper.selectPage(pageInfo,new QueryWrapper<ProductOrderDO>().eq("user_id",loginUser.getId()));
        }

        List<ProductOrderDO> orderDOList = orderDBPage.getRecords();
        List<ProductOrderVO> orderVOList = orderDOList.stream().map(productOrderDO -> {
            List<ProductOrderItemDO> orderItemDOList = productOrderItemMapper.selectList(new QueryWrapper<ProductOrderItemDO>().eq("product_order_id",productOrderDO.getId()));

            List<OrderItemVO> orderItemVOList = orderItemDOList.stream().map(item->{
                OrderItemVO itemVO = new OrderItemVO();
                BeanUtils.copyProperties(item,itemVO);
                return itemVO;
            }).collect(Collectors.toList());
            ProductOrderVO productOrderVO = new ProductOrderVO();
            BeanUtils.copyProperties(productOrderDO,productOrderVO);
            productOrderVO.setOrderItemVOList(orderItemVOList);
            return productOrderVO;
        }
        ).collect(Collectors.toList());

        Map<String,Object> pageMap = new HashMap<>();
        pageMap.put("total_record",orderDBPage.getTotal());
        pageMap.put("total_page",orderDBPage.getPages());
        pageMap.put("current_page",orderVOList);
        return pageMap;
    }

    @Override
    @Transactional
    public JsonData repay(RepayOrderRequest repayOrderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>().eq("out_trade_no",repayOrderRequest.getOutTradeNo()).eq("user_id",loginUser.getId()));
        if (productOrderDO == null) {
            return JsonData.buildResult(BizCodeEnum.PAY_ORDER_NOT_EXIST);
        }

        if(!productOrderDO.getState().equalsIgnoreCase(OrderStatus.NEW.name())){
            return JsonData.buildResult(BizCodeEnum.PAY_ORDER_STATE_ERROR);
        }
        long orderLiveTime = System.currentTimeMillis() - productOrderDO.getCreateTime().getTime();
        orderLiveTime = orderLiveTime +70*1000;
        if (orderLiveTime>TimeConstant.ORDER_PAY_TIMEOUT_MILLS){
            return JsonData.buildResult(BizCodeEnum.PAY_ORDER_PAY_TIMEOUT);
        }
        long timeout = TimeConstant.ORDER_PAY_TIMEOUT_MILLS - orderLiveTime;
        PayInfoVO payInfoVO = new PayInfoVO(productOrderDO.getOutTradeNo(),productOrderDO.getPayAmount(),
                OrderPaymentType.valueOf(repayOrderRequest.getPayType().toUpperCase()),
                ClientType.valueOf(repayOrderRequest.getClientType().toUpperCase()),
                "order out trade no","", timeout);

        // TODO Update payment type and client type for the order

        String payResult = paymentFactory.pay(payInfoVO);
        if (StringUtil.isNotBlank(payResult)){
            return JsonData.buildSuccess(payResult);
        }else{
            return JsonData.buildResult(BizCodeEnum.PAY_ORDER_FAIL);
        }

    }
}
