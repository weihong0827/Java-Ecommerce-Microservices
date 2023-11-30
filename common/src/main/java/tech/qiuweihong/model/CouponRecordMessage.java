package tech.qiuweihong.model;

import lombok.Data;

@Data
public class CouponRecordMessage {
    private Long messageId;

    private String outTradeNo;

    private Long taskId;
}
