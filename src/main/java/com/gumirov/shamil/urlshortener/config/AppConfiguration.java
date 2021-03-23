package com.gumirov.shamil.urlshortener.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration
{
  /**
   * Development configuration profile.
   */
  public final static String DEVELOPMENT = "dev";
  /**
   * Production configuration profile.
   */
  public final static String PRODUNCTION = "prod";

  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public ModelMapper createMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    return mapper;
  }
}
