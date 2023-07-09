package com.curenosm.testproject.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/** Binds these Java Objects from the application.yml file to ease maintainability. */
@Component
@Data
@Validated
@ConfigurationProperties(prefix = "openweathermap")
public class OpenWeatherProps {

  @NotNull @NotBlank private String apiKey;
  private String url;
  private String sslUrl;
}
