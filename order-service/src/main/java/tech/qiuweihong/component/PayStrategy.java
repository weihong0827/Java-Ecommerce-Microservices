package tech.qiuweihong.component;

import tech.qiuweihong.vo.PayInfoVO;

public interface PayStrategy {

    /**
     * Place Order
      * @return
     */
    String unifiedOrder(PayInfoVO payInfoVO);
    default String refund(PayInfoVO payInfoVO){return "";};

    default String queryPaymentStatus(PayInfoVO payInfoVO){return "";};

}
