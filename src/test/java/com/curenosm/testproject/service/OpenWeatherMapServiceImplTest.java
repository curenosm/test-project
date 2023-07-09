package com.curenosm.testproject.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import com.curenosm.testproject.configuration.OpenWeatherProps;
import com.curenosm.testproject.dto.GeocodingServiceResponseDTO;
import com.curenosm.testproject.dto.TimeMachineServiceResponseDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class OpenWeatherMapServiceImplTest {

  @Mock private RestTemplate restTemplate;
  @Autowired private OpenWeatherProps props;

  private OpenWeatherMapService service;

  @BeforeEach
  void setUp() {
    service = new OpenWeatherMapServiceImpl(restTemplate, props);
  }

  @Test
  void testGetMexicoCityCoordinates() {
    GeocodingServiceResponseDTO expected =
        GeocodingServiceResponseDTO.builder()
            .lat("19.42847")
            .lon("-99.12766")
            .zip("06080")
            .country("Mexico")
            .build();

    when(restTemplate.exchange(
            any(String.class), any(HttpMethod.class), any(), any(Class.class), anyMap()))
        .thenReturn(
            ResponseEntity.ok(
                """
      [{
        "lat": "19.42847",
        "lon": "-99.12766",
        "zip": "06080",
        "country": "Mexico"
      }]
      """));

    Optional<GeocodingServiceResponseDTO> coords = service.getCoordinatesByLocationName("New York");

    assert coords.isPresent();
    assert coords.get().equals(expected);
  }

  @Test
  void testGetCoordinatesFromInvalidName() {
    String name = "ThisIsTheNameOfACityThatShould123NotExist";

    assert service.getCoordinatesByLocationName(name).equals(Optional.empty());
  }

  @Test
  void testGetCoordinatesFromSeparatedByCommaLocation() {
    String name = "Mexico City,Mexico";
    GeocodingServiceResponseDTO expected =
        GeocodingServiceResponseDTO.builder()
            .lat("19.42847")
            .lon("-99.12766")
            .zip("06080")
            .country("Mexico")
            .build();

    when(restTemplate.exchange(
            any(String.class), any(HttpMethod.class), any(), any(Class.class), anyMap()))
        .thenReturn(
            ResponseEntity.ok(
                """
      [{
        "lat": "19.42847",
        "lon": "-99.12766",
        "zip": "06080",
        "country": "Mexico"
      }]
      """));

    Optional<GeocodingServiceResponseDTO> res = service.getCoordinatesByLocationName(name);

    assert res.isPresent();
    assert res.get().equals(expected);
  }

  @Test
  void testGetTenDaysReportFromTimeMachineService() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String lat = "19.42847";
    String lon = "-99.12766";
    String time = LocalDate.now().format(formatter);
    String units = "metric";
    TimeMachineServiceResponseDTO expected = new TimeMachineServiceResponseDTO();

    when(restTemplate.exchange(
            any(String.class), any(HttpMethod.class), any(), any(Class.class), anyMap()))
        .thenReturn(
            ResponseEntity.ok(
                """
      [{
        "lat": "19.42847",
        "lon": "-99.12766",
        "zip": "06080",
        "country": "Mexico"
      }]
      """));

    Optional<TimeMachineServiceResponseDTO> res = service.timeMachine(lat, lon, time, units);
    assert res.isPresent();
    assert res.get().equals(expected);
  }
}
