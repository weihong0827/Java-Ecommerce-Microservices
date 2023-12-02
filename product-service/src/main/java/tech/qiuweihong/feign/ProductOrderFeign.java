package tech.qiuweihong.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.qiuweihong.utils.JsonData;

@FeignClient(name="order-service")
public interface ProductOrderFeign {

    @GetMapping ("/api/order/v1/query_state")
    JsonData queryState(@RequestParam("out_trade_no")String outTradeNo);

}
