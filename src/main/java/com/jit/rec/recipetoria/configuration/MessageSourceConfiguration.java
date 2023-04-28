package com.jit.rec.recipetoria.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Properties;
import java.util.Set;

@Configuration
public class MessageSourceConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        Dotenv dotenv = Dotenv.load();

        Properties properties = new Properties();
        Set<DotenvEntry> entries = dotenv.entries();
        for (DotenvEntry entry : entries) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setProperties(properties);
        return configurer;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:validation",
                "classpath:responses",
                "classpath:exceptions"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
