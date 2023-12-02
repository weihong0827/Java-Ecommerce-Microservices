package tech.qiuweihong.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.qiuweihong.request.LockProductRequest;
import tech.qiuweihong.utils.JsonData;

import java.util.List;

@FeignClient(name="product-service")
public interface ProductFeignService {
    /**
     * Confirm order cart item
     * @param productIds
     * @return
     */
    @PostMapping("/api/cart/v1/confirm_order_cart_item")
    JsonData confirmOrderCartItem(@RequestBody List<Long> productIds);

    @PostMapping("/api/product/v1/lock_product")
    JsonData lockProductStock(@RequestBody LockProductRequest lockProductRequest);
}
