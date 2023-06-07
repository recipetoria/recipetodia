package com.jit.rec.recipetoria.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:validation",
                "classpath:responses",
                "classpath:exceptions",
                "classpath:extensions"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
