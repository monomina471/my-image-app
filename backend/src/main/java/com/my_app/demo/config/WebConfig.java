package com.my_app.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig {
    @Configuration
    public class WebMvcConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // "/uploaded/**" というURLでアクセスが来たら
            // プロジェクト直下の "uploads" フォルダの中身を見せる
            registry.addResourceHandler("/uploaded/**")
                    .addResourceLocations("file:../uploads/");
        }
    }

}
