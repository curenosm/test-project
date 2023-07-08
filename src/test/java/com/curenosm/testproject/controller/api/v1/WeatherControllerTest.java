package com.curenosm.testproject.controller.api.v1;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class WeatherControllerTest {

  @Autowired
  private WebTestClient client;

  @Autowired
  ApplicationContext context;

  @Test
  void testGetDataForValidCityReturns200 () {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", "New York")
        .build()
      )
      .headers(headers -> headers.setBasicAuth("user", "password"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.lon").exists()
      .jsonPath("$.lat").exists();
  }

  @Test
  void testGetDataForMultipleValidCitiesReturns200 () {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", List.of("New York", "Chicago", "Mexico City"))
        .build()
      )
      .headers(headers -> headers.setBasicAuth("user", "password"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.lon").exists()
      .jsonPath("$.lat").exists();
  }

  @Test
  void testGetDataMalformedRequestReturns400 () {
    Map<String, String> params = Map.of();
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", List.of())
        .build()
      )
      .headers(headers -> headers.setBasicAuth("user", "password"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError();
  }

  @Test
  void testGetDataForInvalidCityReturns404 () {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", "ThisCityProbablyShouldNotExistAaaaaaaa")
        .build()
      )
      .headers(headers -> headers.setBasicAuth("user", "password"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isNotFound();
  }

  @Test
  void testGetDataForMultipleInvalidCitiesReturns404 () {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", "New York,Chicago,ThisCityProbablyShouldNotExistAaaaaaaa")
        .build()
      )
      .headers(headers -> headers.setBasicAuth("user", "password"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isNotFound();
  }

}
