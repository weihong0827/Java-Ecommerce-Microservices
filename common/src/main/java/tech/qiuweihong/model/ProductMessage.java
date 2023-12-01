package tech.qiuweihong.model;

import lombok.Data;

@Data
public class ProductMessage {
    private long messageId;

    private String outTradeNo;

    private long taskId;
}
