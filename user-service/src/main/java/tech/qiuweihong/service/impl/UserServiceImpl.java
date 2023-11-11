package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.SendCodeEnum;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.model.UserDO;
import tech.qiuweihong.mapper.UserMapper;
import tech.qiuweihong.request.UserLoginRequest;
import tech.qiuweihong.request.UserRegisterRequest;
import tech.qiuweihong.service.NotifyService;
import tech.qiuweihong.service.UserService;
import org.springframework.stereotype.Service;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JWTUtils;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.UserVO;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * User Registe
     * Email verification
     * Password encryption
     * Save to database
     * new user perks
     * @param registerRequest
     * @return
     * */
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private UserMapper userMapper;
    @Override
    public JsonData register(UserRegisterRequest registerRequest) {
        boolean checkCode = false;
        log.info(registerRequest.toString());
        if (StringUtils.isNotBlank(registerRequest.getMail())){
            // check code
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER,registerRequest.getMail(),registerRequest.getCode());
        }
        if (!checkCode){
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(registerRequest,userDO);
        userDO.setCreateTime(new Date());
        userDO.setSlogan("Welcome to my blog");
        userDO.setPoints(0);

        // generate salt
        String salt = "$1$" + CommonUtils.getStringNumRandom(8);
        // encrypt password
        String cryptPwd = Md5Crypt.md5Crypt(userDO.getPwd().getBytes(),salt);
        userDO.setPwd(cryptPwd);
        userDO.setSecret(salt);


        // account unique check
        if(!checkUnique(userDO.getMail())){
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
        log.info(userDO.toString());

        int rows = userMapper.insert(userDO);
        log.info("rows:{},register success{}",rows,userDO.toString());

        // initalize data and give new user perks
        initNewUserData(userDO);
        return JsonData.buildSuccess();


    }

    @Override
    public JsonData login(UserLoginRequest userLoginRequest) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail",userLoginRequest.getEmail());
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if (userDO==null){
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        String encryptedPwd = userDO.getPwd();
        String salt = userDO.getSecret();
        String userEnteredPwdEncrypted = Md5Crypt.md5Crypt(userLoginRequest.getPassword().getBytes(),salt);
        if (!userEnteredPwdEncrypted.equals(encryptedPwd)){
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userDO,loginUser);
        String token = JWTUtils.GenerateToken(loginUser);

        return JsonData.buildSuccess(token);

    }

    @Override
    public UserVO findUserDetail() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        log.info(loginUser.getMail());
        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("id",loginUser.getId()));
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO,userVO);
        return userVO;
    }

    private void initNewUserData(UserDO userDO){

    }
    /**
     * check account unique
     * @param email
     * @return
     * */
    private boolean checkUnique(String email){
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail",email);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if (userDO != null){
            return false;
        }
        return true;

    }
}
