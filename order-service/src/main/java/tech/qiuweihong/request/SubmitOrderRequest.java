package tech.qiuweihong.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SubmitOrderRequest {


    /**
     * Cart coupon
     */
    @JsonProperty("coupon_record_id")
    private Long couponRecordId;

    @JsonProperty("product_id")
    private List<Long> productList;

    @JsonProperty("client_type")
    private String payType;

    @JsonProperty("client_type")
    private String clientType;

    @JsonProperty("address_id")
    private Long addressId;

    @JsonProperty("actual_amount")
    private BigDecimal actualAmount;
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    /**
     * Prevent duplicate order token
     */
    private String token;

}
