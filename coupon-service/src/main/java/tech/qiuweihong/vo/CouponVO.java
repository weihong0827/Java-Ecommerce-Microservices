package tech.qiuweihong.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CouponVO {
    /**
     * id
     */
    private Long id;

    /**
     * Coupon type [NEW_USER registration gift coupon, TASK task coupon, PROMOTION promotional coupon]
     */
    private String category;

    /**
     * Coupon image
     */
    @JsonProperty("coupon_img")
    private String couponImg;

    /**
     * Coupon title
     */
    @JsonProperty("coupon_title")
    private String couponTitle;

    /**
     * Discount price
     */
    private BigDecimal price;

    /**
     * Limit per person
     */

    @JsonProperty("user_limit")
    private Integer userLimit;

    /**
     * Coupon start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale="zh",timezone ="GMT+8")
    @JsonProperty("start_time")
    private Date startTime;

    /**
     * Coupon expiration time
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale="zh",timezone ="GMT+8")
    @JsonProperty("end_time")
    private Date endTime;

    /**
     * Total number of coupons
     */
    @JsonProperty("publish_count")

    private Integer publishCount;

    /**
     * Stock
     */
    private Integer stock;

    /**
     * Creation time of the coupon
     */

    /**
     * Minimum amount required to use
     */
    @JsonProperty("condition_price")
    private BigDecimal conditionPrice;

}
