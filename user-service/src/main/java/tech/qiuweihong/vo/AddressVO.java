package tech.qiuweihong.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AddressVO {
    private Long id;
    /**
     * User id
     */
    private Long userId;

    /**
     * Whether it is the default shipping address: 0->no; 1->yes
     */
    @JsonProperty("default_status")
    private Integer defaultStatus;

    /**
     * Receiver's name
     */
    @JsonProperty("receive_name")
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
    @JsonProperty("detail_address")
    private String detailAddress;
}
