package tech.qiuweihong.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductOrderVO {

    private Long id;

    /**
     * Order unique identifier
     */
    private String outTradeNo;

    /**
     * NEW for unpaid order, PAY for paid order, CANCEL for order canceled due to timeout
     */
    private String state;

    /**
     * Order creation time
     */
    private Date createTime;

    /**
     * Total order amount
     */
    private BigDecimal totalAmount;

    /**
     * Actual order payment price
     */
    private BigDecimal payAmount;

    /**
     * Payment type, such as WeChat, Bank, Alipay
     */
    private String payType;

    /**
     * Nickname
     */
    private String nickname;

    /**
     * Avatar
     */
    private String headImg;

    /**
     * User id
     */
    private Long userId;

    /**
     * 0 for not deleted, 1 for deleted
     */
    private Integer del;

    /**
     * Update time
     */
    private Date updateTime;

    /**
     * Order type DAILY for regular, PROMOTION for promotional
     */
    private String orderType;

    /**
     * Delivery address stored in JSON
     */
    private String receiverAddress;

    private List<OrderItemVO> orderItemVOList;
}
