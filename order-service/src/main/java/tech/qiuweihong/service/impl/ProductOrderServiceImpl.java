package tech.qiuweihong.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.CouponUseStateEnum;
import tech.qiuweihong.feign.CouponFeignService;
import tech.qiuweihong.feign.ProductFeignService;
import tech.qiuweihong.feign.UserFeignService;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.model.ProductOrderDO;
import tech.qiuweihong.mapper.ProductOrderMapper;
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
import java.util.List;
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

        // Create Payment

        return null;
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
}
