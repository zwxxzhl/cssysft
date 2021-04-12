package com.atguigu.utils.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathMapping implements WebMvcConfigurer {

    @Value("${bpmn-path-mapping}")
    private String bpmnPathMapping;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //默认路径映射,必须
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/bpmn/**").addResourceLocations(bpmnPathMapping);
    }
}
