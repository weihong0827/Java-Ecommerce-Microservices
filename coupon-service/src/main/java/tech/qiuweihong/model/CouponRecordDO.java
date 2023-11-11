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
@TableName("coupon_record")
public class CouponRecordDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Coupon id
     */
    private Long couponId;

    /**
     * Time of creation or acquisition
     */
    private Date createTime;

    /**
     * Usage status: available NEW, used USED, expired EXPIRED
     */
    private String useState;

    /**
     * User id
     */
    private Long userId;

    /**
     * User nickname
     */
    private String userName;

    /**
     * Coupon title
     */
    private String couponTitle;

    /**
     * Start time
     */
    private Date startTime;

    /**
     * End time
     */
    private Date endTime;

    /**
     * Order id
     */
    private Long orderId;

    /**
     * Discount price
     */
    private BigDecimal price;

    /**
     * Minimum amount required to use
     */
    private BigDecimal conditionPrice;


}
