package tech.qiuweihong.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@ApiModel(value = "UserRegisterRequest", description = "UserRegisterRequest Object")
@Data
public class UserRegisterRequest {
    @ApiModelProperty(value = "name", example = "qiuweihong")
    private String name;

    @ApiModelProperty(value = "password", example = "12345")
    private String pwd;

    @ApiModelProperty(value = "avatar", example = "http://www.baidu.com")
    @JsonProperty("avatar_url")
    private String headImg;

    @ApiModelProperty(value = "sex", example = "1")
    private Integer sex;

    @ApiModelProperty(value="mail", example = "1176101021@qq.com")
    private String mail;

    @ApiModelProperty(value = "phone", example = "12345678901")
    private String phone;

    @ApiModelProperty(value="code", example = "123456")
    private String code;





}
