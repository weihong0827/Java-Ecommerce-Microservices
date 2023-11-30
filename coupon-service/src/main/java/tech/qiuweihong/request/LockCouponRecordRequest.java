package tech.qiuweihong.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
@ApiModel(value = "Lock Coupon object",description = "Lock Coupon Object")
public class LockCouponRecordRequest {
    @ApiModelProperty(value = "Coupon record ID list",example = "[1,2,3]")
    private List<Long> lockCouponRecodIds;

    @ApiModelProperty(value = "Order ID",example = "[1,2,3]")
    private String orderOutTradeNo;
}
