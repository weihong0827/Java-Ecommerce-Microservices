package tech.qiuweihong.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.SendCodeEnum;
import tech.qiuweihong.service.impl.NotifyServiceImpl;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags = "CaptchaController")
@RestController
@RequestMapping("/api/user/v1")
public class CaptchaController {
    @Autowired
    private Producer captchaProducer;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final long CaptchaExpireTime = 60;
    @Autowired
    private NotifyServiceImpl notifyService;
    @GetMapping("/captcha")
    @ApiOperation(value = "GetCaptcha", notes = "GetCaptcha")
    private void GetCaptcha(HttpServletRequest request,HttpServletResponse response) {
        // 生成验证码字符串
        String capText = captchaProducer.createText();
        log.info("Code:{}", capText);
        // 生成验证码图片并返回图片流
        redisTemplate.opsForValue().set(getCaptchaKey(request), capText, CaptchaExpireTime, TimeUnit.SECONDS);
        BufferedImage bi = captchaProducer.createImage(capText);
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException e){
            log.error("IOException:{}", e.getMessage());
        }
    }

    @ApiOperation(value = "sendRegisterCode", notes = "sendRegisterCode")
    @GetMapping("/sendCode")
    private JsonData sendRegisterCode(@ApiParam("Recipient") @RequestParam(value = "mail",required = true)String mail,
                                     @ApiParam("CaptChaCode") @RequestParam(value = "captcha",required = true)String captcha,
                                      HttpServletRequest request){
        String key = getCaptchaKey(request);
        String cacheCaptcha = redisTemplate.opsForValue().get(key);
        if (captcha != null && captcha.equalsIgnoreCase(cacheCaptcha)){
            redisTemplate.delete(key);
            return notifyService.sendCode(SendCodeEnum.USER_REGISTER,mail);

        }else{
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA);
        }
    }

    private String getCaptchaKey(HttpServletRequest request){
        String ip = CommonUtils.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        String key = "user-service:captcha:"+CommonUtils.MD5(ip+userAgent);

        return key;
    }

}
