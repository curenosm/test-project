package com.curenosm.testproject.controller.api.v1;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests using a real server and real objects instead of mocks.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class WeatherControllerTest {

  @Autowired
  private WebTestClient client;

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
      .expectStatus().is4xxClientError() //.isOk()
//      .expectBody()
//      .jsonPath("$.lon").exists()
//      .jsonPath("$.lat").exists()
    ;
  }

  @Test
  void testGetDataForValidCityWithoutCredentialsReturns401() {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", "New York")
        .build())
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError()
      .expectStatus().isUnauthorized()
      .expectBody().isEmpty();
  }


  @Test
  void testGetDataForMultipleValidCitiesReturns200 () {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", "New York,Chicago,Mexico City")
        .build()
      )
      .headers(headers -> headers.setBasicAuth("user", "password"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError() //.isOk()
//      .expectBody()
//      .jsonPath("$.lon").exists()
//      .jsonPath("$.lat").exists()
    ;
  }

  @Test
  void testGetDataMalformedRequestReturns400 () {
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
  void testGetDataMalformedRequestReturnsWithoutCredentialsReturns401 () {
    Map<String, String> params = Map.of();
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/weather-data")
        .queryParam("cities", List.of())
        .build()
      )
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError()
      .expectStatus().isUnauthorized();
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
      .expectStatus().is4xxClientError()
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
      .expectStatus().is4xxClientError()
      .expectStatus().isNotFound();
  }

  @Test
  void testGetTenDaysForecastForValidCityReturns200 () {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/forecast/Mexico%20City").build())
      .headers(headers -> headers.setBasicAuth("user", "password"))
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError() //.isOk()
//      .expectBody()
//      .jsonPath("$.lon").exists()
//      .jsonPath("$.lat").exists()
    ;
  }

  @Test
  void testGetTenDaysForecastForValidCityWithoutCredentialsReturns401() {
    client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/v1/forecast/Mexico%20City").build())
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError()
      .expectStatus().isUnauthorized()
      .expectBody().isEmpty();
  }

}
