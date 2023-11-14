package tech.qiuweihong.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductVO {
    private Long id;

    /**
     * Title
     */
    private String title;

    /**
     * Cover Image
     */
    @JsonProperty("cover_img")
    private String coverImg;

    /**
     * Details
     */
    private String detail;

    /**
     * Old Price
     */
    @JsonProperty("old_amount")
    private BigDecimal oldAmount;

    /**
     * New Price
     */
    private BigDecimal amount;

    /**
     * Stock
     */
    private Integer stock;
}
