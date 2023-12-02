package tech.qiuweihong.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.qiuweihong.config.AliPayConfig;
import tech.qiuweihong.config.CallBackUrlConfig;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.ClientType;
import tech.qiuweihong.enums.OrderPaymentType;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.service.ProductOrderService;
import tech.qiuweihong.utils.JsonData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-18
 */
@RestController
@RequestMapping("/api/order/v1")
@Slf4j
public class ProductOrderController {
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private CallBackUrlConfig callBackUrlConfig;
    @ApiOperation("Submit Order")
    @PostMapping("")
    public void submitOrder(@ApiParam("order object") @RequestBody SubmitOrderRequest submitOrderRequest, HttpServletResponse response){
        JsonData data = productOrderService.submitOrder(submitOrderRequest);
        if (data.getCode()==0){
            String client = submitOrderRequest.getClientType();
            String payType = submitOrderRequest.getPayType();
            if (payType.equalsIgnoreCase(OrderPaymentType.ALIPAY.name())){
                log.info("Build order success {}",submitOrderRequest.toString());
                if (client.equalsIgnoreCase(ClientType.WEB.name())){
                    writeData(response,data);
                } else if (client.equalsIgnoreCase(ClientType.APP.name())) {

                    // TODO
                }
            } else if (payType.equalsIgnoreCase(OrderPaymentType.WECHAT.name())) {
                // TODO

            }

        }else{
            log.error("build order failed {}",data.toString());
        }

    }
    @ApiOperation("Query order status")
    @GetMapping("/query_state")
    public JsonData queryProductOrderStatus(@RequestParam("out_trade_no")String outTradeNo){
        String state = productOrderService.queryProductOrderState(outTradeNo);
        return StringUtils.isBlank(state)?JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST):JsonData.buildSuccess(state);
    }

    private void writeData(HttpServletResponse response, JsonData data) {
        try {

            response.setContentType("text/html;charset=UTF8");
            response.getWriter().write(data.getData().toString());
            response.getWriter().flush();
            response.getWriter().close();
        }catch (IOException e){
            log.error("Write HTML error {}",e);
        }
    }

    @GetMapping("/alipay")
    public void testAlipay(HttpServletResponse response) throws AlipayApiException, IOException {
        HashMap<String,String>content = new HashMap<>();
        String no = UUID.randomUUID().toString();
        log.info("out trade no:{}",no);
        content.put("out_trade_no", no);
        content.put("product_code", "FAST_INSTANT_TRADE_PAY");
        // order total amount
        content.put("total_amount", String.valueOf("111.99"));
        // title
        content.put("subject", "cup");
        // description
        content.put("body", "good cup");
        // Order ttl, when does the user have to made the payment by, 1m-15d, m-min,h-hour, d-day,1c-current day before 2359, no decimals
        content.put("timeout_express", "5m");
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizContent(JSON.toJSONString(content));
        request.setNotifyUrl(callBackUrlConfig.getAlipayCallbackUrl());
        request.setReturnUrl(callBackUrlConfig.getAlipaySuccessReturnUrl());
        AlipayTradeWapPayResponse tradeAppPayResponse= AliPayConfig.getInstance().pageExecute(request);
        if (tradeAppPayResponse.isSuccess()){
            String form = tradeAppPayResponse.getBody();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();

        }


    }
}

