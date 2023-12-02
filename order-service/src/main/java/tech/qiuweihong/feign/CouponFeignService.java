package tech.qiuweihong.feign;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.qiuweihong.request.LockCouponRecordRequest;
import tech.qiuweihong.utils.JsonData;

@FeignClient(name="coupon-service")
public interface CouponFeignService {
    @GetMapping("/api/coupon_record/v1/{record_id}")
    JsonData findUserCouponRecordById(@PathVariable("record_id") long recordId);
    @PostMapping("/api/coupon_record/v1/lock_records")
    JsonData lockCouponRecord(@ApiParam("Lock Coupon Model")@RequestBody LockCouponRecordRequest lockCouponRecordRequest);
}
