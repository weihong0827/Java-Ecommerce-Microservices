package tech.qiuweihong.model;

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
 * @since 2023-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product_task")
public class ProductTaskDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Product id
     */
    private Long productId;

    /**
     * Purchased Amount
     */
    private Integer buyNum;

    /**
     * Product Name
     */
    private String productName;

    /**
     * Lock State:LOCK,FINISHED,CANCEL
     */
    private String lockState;

    private String outTradeNo;

    private Date createTime;


}
