package tech.qiuweihong.Exception;

import lombok.Data;
import tech.qiuweihong.enums.BizCodeEnum;

@Data
public class BizException extends RuntimeException{
    String msg;
    int code;

    public BizException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(BizCodeEnum bizCodeEnum){
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }

}
