package com.oddle.app.weather;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/**
 * @author akif
 * @since 1/14/22
 */

/**
 * Used to register custom messages.properties file
 */
@Configuration
public class WeatherApplicationConfig {

    @Bean
    public MessageSourceAccessor getMessageSourceAccessor() {
        return new MessageSourceAccessor(messageSource());
    }

    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(this.messageSource());

        return factoryBean;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");

        return messageSource;
    }
}
