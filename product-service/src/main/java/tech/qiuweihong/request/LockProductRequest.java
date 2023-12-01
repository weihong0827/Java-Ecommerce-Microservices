package tech.qiuweihong.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(value = "OrderItemRequest",description = "Order item request")
public class LockProductRequest {
    @ApiModelProperty(
            value = "Order id",
            example = "1"
    )
    @JsonProperty("order_out_trade_no")
    private String orderOutTradeNo;

    @ApiModelProperty(
            value = "Order item list",
            example = "[{\"product_id\":1,\"count\":2}]"
    )
    @JsonProperty("order_item_list")
    private List<OrderItemRequest>orderItemList;



}

