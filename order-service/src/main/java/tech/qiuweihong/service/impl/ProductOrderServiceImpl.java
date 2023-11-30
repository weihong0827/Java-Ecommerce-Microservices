package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.model.ProductOrderDO;
import tech.qiuweihong.mapper.ProductOrderMapper;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.utils.JsonData;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-18
 */
@Service
@Slf4j
public class ProductOrderServiceImpl  implements ProductOrderService {
    @Autowired
    private ProductOrderMapper productOrderMapper;
    @Override
    public JsonData submitOrder(SubmitOrderRequest submitOrderRequest) {
        return null;
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
