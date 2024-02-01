package com.ln.training.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("==>> Config beans file shop/project-sample");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "file:/shop/project-sample/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.DAYS).cachePublic());
    }
}
