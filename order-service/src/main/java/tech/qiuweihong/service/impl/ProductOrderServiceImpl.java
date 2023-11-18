package tech.qiuweihong.service.impl;

import lombok.extern.slf4j.Slf4j;
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

    @Override
    public JsonData submitOrder(SubmitOrderRequest submitOrderRequest) {
        return null;
    }
}
