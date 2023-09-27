package tech.qiuweihong.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="UserLoginRequest",description = "User login request param")
@Data
public class UserLoginRequest {
    @ApiModelProperty(value="email",example = "1176101021qiu@gmail.com")
    private String email;

    @ApiModelProperty(value="password",example = "123456")
    private String password;

}
