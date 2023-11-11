package tech.qiuweihong.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import tech.qiuweihong.request.UserLoginRequest;
import tech.qiuweihong.request.UserRegisterRequest;
import tech.qiuweihong.service.FileService;
import tech.qiuweihong.service.UserService;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.vo.UserVO;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */
@Api(tags = "UserController")
@RestController
@RequestMapping("/api/user/v1")
public class UserController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @ApiOperation(value = "uploadAvatar", notes = "uploadAvatar")
    @PostMapping("/avatar")
    private JsonData uploadAvatar(@ApiParam(value = "file upload",required = true) @RequestPart("file")MultipartFile file){
        String url = fileService.uploadFile(file);
        return url!= null ? JsonData.buildSuccess(url) : JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_AVATAR_FAIL);

    }
    @ApiOperation(value = "register", notes = "register")
    @PostMapping("register")
    private JsonData register(@ApiParam("register object") @RequestBody UserRegisterRequest registerRequest){
        JsonData jsonData = userService.register(registerRequest);
        return jsonData;
    }
    @ApiOperation("User details")
    @GetMapping("detail")
    public JsonData detail(){
        UserVO userVo = userService.findUserDetail();
        return JsonData.buildSuccess(userVo);
    }

    @ApiOperation(value="login",notes = "Login")
    @PostMapping("login")
    private JsonData login(@ApiParam("Login Object") @RequestBody UserLoginRequest loginRequest){
        JsonData jsonData = userService.login(loginRequest);
        return jsonData;

    }

}

