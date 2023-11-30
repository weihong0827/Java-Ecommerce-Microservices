package tech.qiuweihong.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.ClientType;
import tech.qiuweihong.enums.OrderPaymentType;
import tech.qiuweihong.request.SubmitOrderRequest;
import tech.qiuweihong.service.ProductOrderService;
import tech.qiuweihong.utils.JsonData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
}

