package org.safetrust.managecontacts.config;

import org.safetrust.managecontacts.interceptor.LoggingInterceptor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebConfig implements WebMvcConfigurer {

  /**
   * Configures and adds custom interceptors to the application's interceptor registry.
   *
   * @param registry the InterceptorRegistry object used to register one or more interceptors.
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoggingInterceptor());
  }

  /**
   * Configures the public API documentation group for OpenAPI.
   *
   * @return a GroupedOpenApi object that defines the public API documentation group, including
   *     specific paths and packages to include in the documentation.
   */
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("contact").pathsToMatch("/api/contact/**").build();
  }
}
