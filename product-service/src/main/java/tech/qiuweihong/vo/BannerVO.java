package tech.qiuweihong.vo;

import lombok.Data;

@Data
public class BannerVO {
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
