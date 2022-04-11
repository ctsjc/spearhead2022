package com.jitendra.javaspearhead.model.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class ModelConfig {

    @Bean
    Resource loadResource(){
        ClassPathResource classPathResource=new ClassPathResource("");
        System.out.println(classPathResource.getPath());
        return classPathResource;
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
