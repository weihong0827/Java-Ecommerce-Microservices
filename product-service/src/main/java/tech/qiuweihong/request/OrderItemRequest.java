package tech.qiuweihong.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderItemRequest",description = "Order item request")
public class OrderItemRequest {

    @ApiModelProperty(
            value = "Product id",
            example = "1"
    )
    @JsonProperty("product_id")
    private Long productId;


    @ApiModelProperty(
            value = "Product count",
            example = "2"
    )
    @JsonProperty("count")
    private Integer count;
}
