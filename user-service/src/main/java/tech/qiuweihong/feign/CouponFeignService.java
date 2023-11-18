package tech.qiuweihong.feign;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.qiuweihong.request.NewUserCouponRequest;
import tech.qiuweihong.utils.JsonData;

@FeignClient(name="coupon-service")
public interface CouponFeignService {
    @PostMapping("/api/coupon/v1/new_user_coupon")
    JsonData claimNewUserCoupon( @RequestBody NewUserCouponRequest newUserCouponRequest);
}
