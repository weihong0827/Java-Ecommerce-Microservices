package tech.qiuweihong.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JWTUtils;
import tech.qiuweihong.utils.JsonData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<LoginUser> threadLocal= new ThreadLocal<LoginUser>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Retrieve the token from the "Authorization" header
        String accessToken = request.getHeader("JWT");

        // Check if the Authorization header is present and starts with "Bearer"
        if (StringUtils.isNotBlank(accessToken)){
            Claims claims = JWTUtils.verify(accessToken);
            if (claims == null){
                CommonUtils.sendJsonMessage(response,JsonData.buildResult(BizCodeEnum.USER_NOT_LOGIN));
                return false;
            }
            Integer userId = Integer.valueOf(claims.get("id").toString());
            String name = (String)claims.get("name");
            String mail = (String)claims.get("mail");
            LoginUser loginUser= new LoginUser();
            loginUser.setId(userId);
            loginUser.setName(name);
            loginUser.setMail(mail);
            threadLocal.set(loginUser);
            return true;

        }
        CommonUtils.sendJsonMessage(response,JsonData.buildResult(BizCodeEnum.USER_NOT_LOGIN));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
