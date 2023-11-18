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
 * @since 2023-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product_order")
public class ProductOrderDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Order unique identifier
     */
    private String outTradeNo;

    /**
     * NEW for unpaid order, PAY for paid order, CANCEL for order canceled due to timeout
     */
    private String state;

    /**
     * Order creation time
     */
    private Date createTime;

    /**
     * Total order amount
     */
    private BigDecimal totalAmount;

    /**
     * Actual order payment price
     */
    private BigDecimal payAmount;

    /**
     * Payment type, such as WeChat, Bank, Alipay
     */
    private String payType;

    /**
     * Nickname
     */
    private String nickname;

    /**
     * Avatar
     */
    private String headImg;

    /**
     * User id
     */
    private Integer userId;

    /**
     * 0 for not deleted, 1 for deleted
     */
    private Integer del;

    /**
     * Update time
     */
    private Date updateTime;

    /**
     * Order type DAILY for regular, PROMOTION for promotional
     */
    private String orderType;

    /**
     * Delivery address stored in JSON
     */
    private String receiverAddress;


}
