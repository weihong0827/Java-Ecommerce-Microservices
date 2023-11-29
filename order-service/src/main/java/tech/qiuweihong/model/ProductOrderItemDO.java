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
@TableName("product_order_item")
public class ProductOrderItemDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Order number
     */
    private Long productOrderId;

    private String outTradeNo;

    /**
     * Product id
     */
    private Long productId;

    /**
     * Product name
     */
    private String productName;

    /**
     * Product image
     */
    private String productImg;

    /**
     * Purchase quantity
     */
    private Integer buyNum;

    private Date createTime;

    /**
     * Total price of shopping item
     */
    private BigDecimal totalAmount;

    /**
     * Unit price of shopping item
     */
    private BigDecimal amount;


}
