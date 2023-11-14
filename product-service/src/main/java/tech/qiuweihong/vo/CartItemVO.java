package tech.qiuweihong.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartItemVO {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("buy_num")
    private Integer buyNum;

    @JsonProperty("product_title")
    private String productTitle;

    @JsonProperty("product_img")
    private String productImg;

    private BigDecimal amount;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    public BigDecimal getTotalAmount() {
        return this.amount.multiply(BigDecimal.valueOf(this.buyNum));
    }

}
