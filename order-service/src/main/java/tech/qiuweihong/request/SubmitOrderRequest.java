package tech.qiuweihong.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value ="SubmitOrderRequest",description="Submit order request")
public class SubmitOrderRequest {


    /**
     * Cart coupon
     */
    @ApiModelProperty(value = "Coupon record id",example = "1")
    @JsonProperty("coupon_record_id")
    private Long couponRecordId;


    @ApiModelProperty(value = "Order item list",example = "[{\"product_id\":1,\"count\":2}]")
    @JsonProperty("product_id")
    private List<Long> productList;

    @ApiModelProperty(value = "Payment type",example = "Alipay")
    @JsonProperty("pay_type")
    private String payType;

    @ApiModelProperty(value = "Client type",example = "PC")
    @JsonProperty("client_type")
    private String clientType;

    @ApiModelProperty(value = "Address id",example = "1")
    @JsonProperty("address_id")
    private Long addressId;

    @ApiModelProperty(value = "Actual amount",example = "1")
    @JsonProperty("actual_amount")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "Total amount",example = "1")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    /**
     * Prevent duplicate order token
     */
    private String token;

}
