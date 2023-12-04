package tech.qiuweihong.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import tech.qiuweihong.config.AlipayConfig;
import tech.qiuweihong.config.CallBackUrlConfig;
import tech.qiuweihong.constants.CacheKey;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.ClientType;
import tech.qiuweihong.enums.OrderPaymentType;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.request.RepayOrderRequest;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.service.ProductOrderService;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private StringRedisTemplate redisTemplate;

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
            CommonUtils.sendJsonMessage(response,data);
        }
    }
    @ApiOperation("Repay")
    @PostMapping("/repay")
    public void repay(@ApiParam("order object") @RequestBody RepayOrderRequest repayOrderRequest, HttpServletResponse response){
        JsonData data = productOrderService.repay(repayOrderRequest);
        if (data.getCode()==0){
            String client = repayOrderRequest.getClientType();
            String payType = repayOrderRequest.getPayType();
            if (payType.equalsIgnoreCase(OrderPaymentType.ALIPAY.name())){
                log.info("Build order success {}",repayOrderRequest.toString());
                if (client.equalsIgnoreCase(ClientType.WEB.name())){
                    writeData(response,data);
                } else if (client.equalsIgnoreCase(ClientType.APP.name())) {

                    // TODO
                }
            } else if (payType.equalsIgnoreCase(OrderPaymentType.WECHAT.name())) {
                // TODO

            }

        }else{
            log.error("repay order failed {}",data.toString());
            CommonUtils.sendJsonMessage(response,data);
        }

    }
    @ApiOperation("Get Submit Order token")
    @GetMapping("get_token")
    public JsonData get_token(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String key = String.format(CacheKey.OrderKey,loginUser.getId());
        String token = CommonUtils.getStringNumRandom(32);
        redisTemplate.opsForValue().set(key,token, 30,TimeUnit.MINUTES);
        return JsonData.buildSuccess(token);

    }

    @ApiOperation("Query order status")
    @GetMapping("/query_state")
    public JsonData queryProductOrderStatus(@RequestParam("out_trade_no")String outTradeNo){
        String state = productOrderService.queryProductOrderState(outTradeNo);
        return StringUtils.isBlank(state)?JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST):JsonData.buildSuccess(state);
    }

    @ApiOperation("Paginated personal order check")
    @GetMapping("/")
    public JsonData pageOrderList(
            @ApiParam(value="Current page")  @RequestParam(value = "page",defaultValue = "1") int page,
            @ApiParam(value="Page size") @RequestParam(value="size",defaultValue = "20") int size,
            @ApiParam(value="Order State") @RequestParam(value="state",required = false) String state
    ){
        Map<String,Object> details = productOrderService.detail(page, size,state);
        return JsonData.buildSuccess(details);
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
        AlipayTradeWapPayResponse tradeAppPayResponse= AlipayConfig.getInstance().pageExecute(request);
        if (tradeAppPayResponse.isSuccess()){
            String form = tradeAppPayResponse.getBody();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();

        }


    }
}

