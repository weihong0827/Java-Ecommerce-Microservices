package tech.qiuweihong.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("banner")
public class BannerDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Image
     */
    private String img;

    /**
     * Jump Address
     */
    private String url;

    /**
     * Weight
     */
    private Integer weight;


}
