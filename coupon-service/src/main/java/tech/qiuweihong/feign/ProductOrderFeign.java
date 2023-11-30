package tech.qiuweihong.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.qiuweihong.utils.JsonData;

@FeignClient(name="order-service")
public interface ProductOrderFeign {
    /**
     * Query order state
     * @param outTradeNo
     * @return
     */
    @GetMapping("api/order/v1/query_state")
    JsonData queryProductOrderStatus(@RequestParam("out_trade_no")String outTradeNo);
}
