package tech.qiuweihong.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("coupon")
public class CouponDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Coupon type [NEW_USER registration gift coupon, TASK task coupon, PROMOTION promotional coupon]
     */
    private String category;

    /**
     * Publish status, PUBLISH published, DRAFT draft, OFFLINE offline
     */
    private String publish;

    /**
     * Coupon image
     */
    private String couponImg;

    /**
     * Coupon title
     */
    private String couponTitle;

    /**
     * Discount price
     */
    private BigDecimal price;

    /**
     * Limit per person
     */
    private Integer userLimit;

    /**
     * Coupon start time
     */
    private Date startTime;

    /**
     * Coupon expiration time
     */
    private Date endTime;

    /**
     * Total number of coupons
     */
    private Integer publishCount;

    /**
     * Stock
     */
    private Integer stock;

    /**
     * Creation time of the coupon
     */
    private Date createTime;

    /**
     * Minimum amount required to use
     */
    private BigDecimal conditionPrice;


}
