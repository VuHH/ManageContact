package org.safetrust.managecontacts.config;

import org.safetrust.managecontacts.interceptor.LoggingInterceptor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoggingInterceptor());
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("contact").pathsToMatch("/api/contact/**").build();
  }
}
