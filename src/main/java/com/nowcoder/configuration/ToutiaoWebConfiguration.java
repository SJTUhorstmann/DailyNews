package com.nowcoder.configuration;


import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Created by lenovo on 2019/8/10.
 */
public class ToutiaoWebConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        super.addInterceptors(registry);
    }
}
