package tech.qiuweihong.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;

    /**
     * Nickname
     */
    private String name;

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

    /**
     * Email
     */
    private String mail;


}
