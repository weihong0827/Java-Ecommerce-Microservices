package tech.qiuweihong.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.feign.UserFeignService;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.model.ProductOrderDO;
import tech.qiuweihong.mapper.ProductOrderMapper;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.ProductOrderAddressVO;

@Service
@Slf4j
public class ProductOrderServiceImpl  implements ProductOrderService {
    @Autowired
    private ProductOrderMapper productOrderMapper;
    @Autowired
    private UserFeignService userFeignService;
    @Override
    public JsonData submitOrder(SubmitOrderRequest submitOrderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String outTradeNo = CommonUtils.getStringNumRandom(32);
        ProductOrderAddressVO addressVO = this.getUserAddress(submitOrderRequest.getAddressId());
        log.info("address detail:{}",addressVO);
        return null;
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
