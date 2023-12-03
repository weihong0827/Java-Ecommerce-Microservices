package tech.qiuweihong.component;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.config.AlipayConfig;
import tech.qiuweihong.config.CallBackUrlConfig;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.ClientType;
import tech.qiuweihong.vo.PayInfoVO;

import java.util.HashMap;

@Service
@Slf4j
public class AlipayStrategy implements PayStrategy{
    @Autowired
    private CallBackUrlConfig callBackUrlConfig;

    /**
     * Generates a unified order for payment.
     *
     * @param payInfoVO the payment information.
     * @return the payment form to be rendered on the front-end.
     * @throws BizException if the order pay timeout is less than 1 minute.
     */
    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        HashMap<String,String> content = new HashMap<>();

        content.put("out_trade_no", payInfoVO.getOutTradeNo());

        content.put("product_code", "FAST_INSTANT_TRADE_PAY");
        // order total amount
        content.put("total_amount", String.valueOf(payInfoVO.getPayFee()));
        // title
        content.put("subject", payInfoVO.getTitle());
        // description
        content.put("body", payInfoVO.getDescription());

        double timeout = Math.floor(payInfoVO.getOrderPayTimeoutMillis()/(1000*60));
        if (timeout < 1) {
            throw new BizException(BizCodeEnum.PAY_ORDER_PAY_TIMEOUT);
        }

        // Order ttl, when does the user have to made the payment by, 1m-15d, m-min,h-hour, d-day,1c-current day before 2359, no decimals
        content.put("timeout_express", Double.valueOf(timeout)+"m");

        ClientType clientType = payInfoVO.getClientType();
        String form = "";

        try{
            // TODO: Can be optimized by using a factory pattern
            if (clientType.equals(ClientType.WEB)){
                AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
                request.setBizContent(JSON.toJSONString(content));
                request.setNotifyUrl(callBackUrlConfig.getAlipayCallbackUrl());
                request.setReturnUrl(callBackUrlConfig.getAlipaySuccessReturnUrl());
                AlipayTradeWapPayResponse tradeAppPayResponse= AlipayConfig.getInstance().pageExecute(request);
                if (tradeAppPayResponse.isSuccess()){
                    form = tradeAppPayResponse.getBody();
                }else {
                    log.error("Alipay unified order error, outTradeNo: {}, code: {}, msg: {}",
                            payInfoVO.getOutTradeNo(), tradeAppPayResponse.getCode(), tradeAppPayResponse.getMsg());
                }
            }else if (clientType.equals(ClientType.APP)){
                // TODO
            }else if (clientType.equals(ClientType.PC)){
                // TODO
            }

        }catch (AlipayApiException e) {
            log.error("Alipay unified order error, outTradeNo: {}, code: {}, msg: {}",
                    payInfoVO.getOutTradeNo(), e.getErrCode(), e.getErrMsg());
        }

        return form;
    }

    @Override
    public String queryPaymentStatus(PayInfoVO payInfoVO) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        HashMap<String,String> content = new HashMap<>();

        content.put("out_trade_no",payInfoVO.getOutTradeNo());
        request.setBizContent(JSON.toJSONString(content));
        AlipayTradeQueryResponse response = null;
        try {
            response = AlipayConfig.getInstance().execute(request);
        } catch (AlipayApiException e) {
            log.error("Alipay query error:{}",e);

        }
        if (response.isSuccess()){
            return response.getTradeStatus();
        }else{
            return "";
        }
    }
}
