package tech.qiuweihong.service;

import tech.qiuweihong.enums.OrderPaymentType;
import tech.qiuweihong.model.OrderMessage;
import tech.qiuweihong.model.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.utils.JsonData;

import java.util.Map;

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

    String queryProductOrderState(String outTradeNo);

    boolean closeProductOrder(OrderMessage orderMessage);

    JsonData handleOrderCallbackMsg(OrderPaymentType orderPaymentType, Map<String, String> paramMap);
}
