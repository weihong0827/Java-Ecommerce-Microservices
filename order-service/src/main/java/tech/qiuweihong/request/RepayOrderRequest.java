package tech.qiuweihong.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Repay param")
public class RepayOrderRequest {

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @ApiModelProperty(value = "Payment type",example = "Alipay")
    @JsonProperty("pay_type")
    private String payType;

    @ApiModelProperty(value = "Client type",example = "PC")
    @JsonProperty("client_type")
    private String clientType;

}
