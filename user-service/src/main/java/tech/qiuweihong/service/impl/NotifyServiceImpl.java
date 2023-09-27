package tech.qiuweihong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tech.qiuweihong.constants.CacheKey;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.SendCodeEnum;
import tech.qiuweihong.component.MailService;
import tech.qiuweihong.service.NotifyService;
import tech.qiuweihong.utils.CheckUtils;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private MailService mailService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String SUBJECT = "Your verification code";
    private static final int CODE_EXPIRE = 60 * 1000;
    private static final String CONTENT = "Your verification code is:%s , it expires in 60s, please do not tell others. ";
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String mail) {
        String cacheKey  = String.format(CacheKey.CacheCodeKey,sendCodeEnum.name(),mail);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(cacheValue)){
            long expire = Long.parseLong(cacheValue.split("_")[1]);
            if (System.currentTimeMillis() - expire < CODE_EXPIRE){
                // code is still valid cannot request for new code
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }

        if(CheckUtils.isEmail(mail)){
            String code = CommonUtils.getRandomCode(6);
            mailService.sendMail(mail,SUBJECT,String.format(CONTENT, code));
            String value = code + "_" + System.currentTimeMillis();
            redisTemplate.opsForValue().set(cacheKey,value,CODE_EXPIRE, TimeUnit.MILLISECONDS);
            return JsonData.buildSuccess();
        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }

    /**
     * Check verification code
     * @param sendCodeEnum
     * @param mail
     * @param code
     * @return
     */
    @Override
    public boolean checkCode(SendCodeEnum sendCodeEnum, String mail, String code) {
        String cacheKey = String.format(CacheKey.CacheCodeKey,sendCodeEnum.name(),mail);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(cacheValue)){
            String cacheCode = cacheValue.split("_")[0];
            if (cacheCode.equals(code)){
                // delete code after verification
                redisTemplate.delete(cacheKey);
                return true;
            }
        }
        return false;
    }


}
