package tech.qiuweihong.service;

import tech.qiuweihong.enums.SendCodeEnum;
import tech.qiuweihong.utils.JsonData;

public interface NotifyService {
    /**
     * Send verification code
     * @param sendCodeEnum
     * @param mail
     * @return
     */
    JsonData sendCode(SendCodeEnum sendCodeEnum, String mail);
    /**
     * Check verification code
     */
    boolean checkCode(SendCodeEnum sendCodeEnum, String mail, String code);

}
