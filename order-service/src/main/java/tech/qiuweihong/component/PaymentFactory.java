package tech.qiuweihong.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.qiuweihong.enums.OrderPaymentType;
import tech.qiuweihong.vo.PayInfoVO;

@Component
public class PaymentFactory {
    @Autowired
    private AlipayStrategy alipayStrategy;

    @Autowired
    private WechatPayStrategy wechatPayStrategy;

    /**
     * Executes the payment for a given PayInfoVO object.
     *
     * @param payInfoVO The PayInfoVO object containing the necessary payment information.
     * @return A string representing the result of the payment.
     */
    public String pay(PayInfoVO payInfoVO){
        OrderPaymentType paymentType = payInfoVO.getPaymentType();

        if (paymentType.equals(OrderPaymentType.ALIPAY)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
            return payStrategyContext.executeUnifiedOrder(payInfoVO);
        }else if (paymentType.equals(OrderPaymentType.WECHAT)){
            // Not yet implement
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeUnifiedOrder(payInfoVO);
        }

        return null;

    }


    /**
     * Executes a query to check the payment status for a given PayInfoVO object.
     *
     * @param payInfoVO The PayInfoVO object containing the necessary payment information.
     * @return A string representing the payment status.
     */
    public String queryPaymentStatus(PayInfoVO payInfoVO){
        OrderPaymentType paymentType = payInfoVO.getPaymentType();

        if (paymentType.equals(OrderPaymentType.ALIPAY)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
            return payStrategyContext.executeQueryPaymentStatus(payInfoVO);
        }else if (paymentType.equals(OrderPaymentType.WECHAT)){
            // Not yet implement
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeQueryPaymentStatus(payInfoVO);
        }

        return null;
    }

}
