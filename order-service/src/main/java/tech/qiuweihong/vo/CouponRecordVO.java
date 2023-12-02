package tech.qiuweihong.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class CouponRecordVO {
    private Long id;

    /**
     * Coupon id
     */
    @JsonProperty("coupon_id")
    private Long couponId;


    /**
     * Usage status: available NEW, used USED, expired EXPIRED
     */
    @JsonProperty("use_state")
    private String useState;

    /**
     * User id
     */
    @JsonProperty("user_id")
    private Long userId;

    /**
     * User nickname
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * Coupon title
     */
    @JsonProperty("coupon_title")
    private String couponTitle;

    /**
     * Start time
     */
    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale="zh",timezone ="GMT+8")
    private Date startTime;

    /**
     * End time
     */
    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale="zh",timezone ="GMT+8")
    private Date endTime;

    /**
     * Order id
     */

    @JsonProperty("order_id")
    private Long orderId;

    /**
     * Discount price
     */
    private BigDecimal price;

    /**
     * Minimum amount required to use
     */
    @JsonProperty("condition_price")
    private BigDecimal conditionPrice;
}
