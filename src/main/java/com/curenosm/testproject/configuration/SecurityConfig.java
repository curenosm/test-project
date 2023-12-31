package com.curenosm.testproject.configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

/** Configuration class intended to store all authentication and authorization settings. */
@Slf4j
@Configuration
public class SecurityConfig {

  @Value("${spring.security.user.name}")
  public String user;

  @Value("${spring.security.user.password}")
  public String password;

  @Bean
  @Qualifier("openWeatherClient")
  public RestTemplate openWeatherClient() {
    return new RestTemplate();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(config -> config.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  /**
   * This is meant to be executed at the start of the application so that the authorization token is
   * displayed in the console.
   */
  @Bean
  @Order(2)
  public ApplicationRunner showBearerTokenInScreen() {
    return args -> {
      String token =
          Base64.getEncoder()
              .encodeToString((user + ":" + password).getBytes(StandardCharsets.UTF_8));
      System.out.printf(
          """
        This is the "Authorization" header bearer token that you can use to authenticate: %s
        User: %s
        Password: %s%n
        """,
          "Basic " + token, user, password);
    };
  }
}
