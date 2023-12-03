package tech.qiuweihong.component;

import tech.qiuweihong.vo.PayInfoVO;

public class PayStrategyContext {

    private PayStrategy payStrategy;

    public PayStrategyContext(PayStrategy payStrategy){
        this.payStrategy = payStrategy;
    }

    /**
     * Executes the unified order for a given PayInfoVO object using the current payment strategy.
     *
     * @param payInfoVO The PayInfoVO object containing the necessary payment information.
     * @return A string representing the result of the unified order.
     */
    public String executeUnifiedOrder(PayInfoVO payInfoVO){
        return this.payStrategy.unifiedOrder(payInfoVO);
    }

    /**
     * Executes a query to check the payment status for a given PayInfoVO object.
     *
     * @param payInfoVO The PayInfoVO object containing the necessary payment information.
     * @return A string representing the payment status.
     */
    public String executeQueryPaymentStatus(PayInfoVO payInfoVO){
        return this.payStrategy.queryPaymentStatus(payInfoVO);
    }
}
