package tech.qiuweihong.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.qiuweihong.utils.JsonData;

/**
 * <p>
 * Feign service interface for interacting with the user-service.
 * </p>
 */
@FeignClient(name="user-service")
public interface UserFeignService {

    @GetMapping("/api/address/v1/{address_id}")
    JsonData getUserAddress(@PathVariable("address_id")Long addressId);


}
