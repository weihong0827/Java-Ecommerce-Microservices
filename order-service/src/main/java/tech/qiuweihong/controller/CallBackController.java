package tech.qiuweihong.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.qiuweihong.config.AlipayConfig;
import tech.qiuweihong.enums.OrderPaymentType;
import tech.qiuweihong.service.ProductOrderService;
import tech.qiuweihong.utils.JsonData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Api("Order payment controller")
@Controller
@RequestMapping("/api/order/v1/callback")
@Slf4j
public class CallBackController {
    @Autowired
    private ProductOrderService productOrderService;

    @PostMapping("/alipay")
    public String alipayCallback(HttpServletResponse response, HttpServletRequest request){
        Map<String,String> paramMap = convertRequestParamsToMap(request);
        try {
            boolean signedVerified  = AlipaySignature.rsaCertCheckV1(paramMap, AlipayConfig.ALIPAY_PUB_KEY,AlipayConfig.CHARSET,AlipayConfig.SIGN_TYPE);
            if (signedVerified){
                JsonData data = productOrderService.handleOrderCallbackMsg(OrderPaymentType.ALIPAY,paramMap);
                if (data.getCode()==0){
                    // Send success back to alipay
                    return "success";
                }
            }
        }catch (AlipayApiException e){
            log.info("Alipay signature verification error:{} oarams:{}",e,paramMap);
        }

        return "failure";

    }

    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>(16);
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int size = values.length;
            if (size == 1) {
                paramsMap.put(name, values[0]);
            } else {
                paramsMap.put(name, "");
            }
        }
        System.out.println(paramsMap);
        return paramsMap;
    }
}
