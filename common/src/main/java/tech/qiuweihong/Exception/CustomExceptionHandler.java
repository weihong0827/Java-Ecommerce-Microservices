package tech.qiuweihong.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.qiuweihong.utils.JsonData;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData handle(Exception e){
        log.error("[Exception]{}",e);
        if(e instanceof BizException){
            BizException bizException = (BizException) e;
            return JsonData.buildCodeAndMsg(bizException.getCode(),bizException.getMsg());
        }else{
            return JsonData.buildCodeAndMsg(-1,"Unknown Error");
        }
    }
}
