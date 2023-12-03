package tech.qiuweihong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiuweihong.enums.ClientType;
import tech.qiuweihong.enums.OrderPaymentType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayInfoVO {
    private String outTradeNo;

    private BigDecimal payFee;

    private OrderPaymentType paymentType;

    private ClientType clientType;

    private String title;

    private String description;

    private long orderPayTimeoutMillis;

}
