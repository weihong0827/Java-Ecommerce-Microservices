package tech.qiuweihong.Exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.springframework.stereotype.Component;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        JsonData data = null;
        if (e instanceof FlowException) {
            data = JsonData.buildResult(BizCodeEnum.CONTROL_FLOW);
        }else if (e instanceof DegradeException){
            data = JsonData.buildResult(BizCodeEnum.CONTROL_DEGRADE);
        }else if (e instanceof AuthorityException){
            data = JsonData.buildResult(BizCodeEnum.CONTROL_AUTH);
        }
        httpServletResponse.setStatus(200);
        CommonUtils.sendJsonMessage(httpServletResponse,data);

    }
}
