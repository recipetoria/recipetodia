package com.jit.rec.recipetoria.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Properties;
import java.util.Set;

public class DotenvConfig {

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
}
