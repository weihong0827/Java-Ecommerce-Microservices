package tech.qiuweihong.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Address Object", description="Add address fields")
public class AddressAddRequest {
    @ApiModelProperty(value="Whether it is the default address 0->No, 1-> Yes",example ="0")
    @JsonProperty("default_status")
    private Integer defaultStatus;

    /**
     * Receiver's name
     */
    @ApiModelProperty(value="recipient name",example ="qiu weihong")
    @JsonProperty("receive_name")
    private String receiveName;

    /**
     * Receiver's phone
     */
    @ApiModelProperty(value="receiver phone number",example ="12345678")
    private String phone;

    /**
     * Province/municipality
     */
    @ApiModelProperty(value="province",example ="fujian")
    private String province;

    /**
     * City
     */
    @ApiModelProperty(value="city",example ="fuzhou")
    private String city;

    /**
     * District
     */
    @ApiModelProperty(value="district",example ="somewhere")
    private String region;

    /**
     * Detailed address
     */
    @ApiModelProperty(value="address",example ="more detailed address")
    private String detailAddress;

}
