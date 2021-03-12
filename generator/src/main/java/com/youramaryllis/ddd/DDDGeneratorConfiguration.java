package com.youramaryllis.ddd;

import com.youramaryllis.ddd.contextMap.generator.ContextMapGenerator;
import com.youramaryllis.ddd.domainModel.generator.DomainModelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DDDGeneratorProperties.class)
public class DDDGeneratorConfiguration {

    @Autowired DDDGeneratorProperties properties;

    @Bean
    ContextMapGenerator getContextMapGenerator() {
        return new ContextMapGenerator(properties.getPackageName());
    }

    @Bean
    DomainModelGenerator getDomainModelGenerator() {
        return new DomainModelGenerator(properties.getPackageName());
    }
}
