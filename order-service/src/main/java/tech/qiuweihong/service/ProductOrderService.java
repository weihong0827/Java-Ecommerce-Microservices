package tech.qiuweihong.service;

import tech.qiuweihong.model.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-18
 */
public interface ProductOrderService {

    JsonData submitOrder(SubmitOrderRequest submitOrderRequest);
}
