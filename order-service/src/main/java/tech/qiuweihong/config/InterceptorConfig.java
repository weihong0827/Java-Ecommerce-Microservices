package tech.qiuweihong.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.qiuweihong.interceptor.LoginInterceptor;

@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                //拦截的路径
                .addPathPatterns("/api/order/*/**/")
                //排查不拦截的路径
                .excludePathPatterns("/api/callback/*/**","/api/order/*/query_state");
    }
}
