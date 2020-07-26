package com.suprateam.car.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableSpringDataWebSupport
public class LocaleConfiguration implements WebMvcConfigurer {


    private String corsOrigin="*";

    private String corsMapping="/**";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry
                .addMapping(corsMapping)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
                .allowedOrigins(corsOrigin);
    }

}
