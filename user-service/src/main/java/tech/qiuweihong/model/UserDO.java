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
@TableName("user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Nickname
     */
    private String name;

    /**
     * Password
     */
    private String pwd;

    /**
     * Avatar
     */
    private String headImg;

    /**
     * User signature
     */
    private String slogan;

    /**
     * 0 represents female, 1 represents male
     */
    private Integer sex;

    /**
     * Points
     */
    private Integer points;

    private Date createTime;

    /**
     * Email
     */
    private String mail;

    /**
     * Salt, used for personal sensitive information processing
     */
    private String secret;


}
