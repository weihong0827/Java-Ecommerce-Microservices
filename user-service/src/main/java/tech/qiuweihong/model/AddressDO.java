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
 * @since 2023-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("address")
public class AddressDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User id
     */
    private Long userId;

    /**
     * Whether it is the default shipping address: 0->no; 1->yes
     */
    private Integer defaultStatus;

    /**
     * Receiver's name
     */
    private String receiveName;

    /**
     * Receiver's phone
     */
    private String phone;

    /**
     * Province/municipality
     */
    private String province;

    /**
     * City
     */
    private String city;

    /**
     * District
     */
    private String region;

    /**
     * Detailed address
     */
    private String detailAddress;

    private Date createTime;


}
