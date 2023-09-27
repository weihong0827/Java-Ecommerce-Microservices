package tech.qiuweihong.service;

import tech.qiuweihong.request.UserLoginRequest;
import tech.qiuweihong.request.UserRegisterRequest;
import tech.qiuweihong.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */
public interface UserService {
    JsonData register(UserRegisterRequest registerRequest);
    JsonData login(UserLoginRequest userLoginRequest);

}
