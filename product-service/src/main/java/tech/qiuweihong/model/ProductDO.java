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
 * @since 2023-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product")
public class ProductDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Title
     */
    private String title;

    /**
     * Cover Image
     */
    private String coverImg;

    /**
     * Details
     */
    private String detail;

    /**
     * Old Price
     */
    private BigDecimal oldAmount;

    /**
     * New Price
     */
    private BigDecimal amount;

    /**
     * Stock
     */
    private Integer stock;

    /**
     * Creation Time
     */
    private Date createTime;

    /**
     * Locked Stock
     */
    private Integer lockStock;


}
