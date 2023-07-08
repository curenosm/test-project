package com.curenosm.testproject.service;


import static org.mockito.ArgumentMatchers.any;

import com.curenosm.testproject.dto.GeocodingServiceResponseDTO;
import com.curenosm.testproject.exceptions.LocationNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenWeatherMapServiceImplTest {

  @Mock
  private OpenWeatherMapServiceImpl service;

  @Test
  void testGetNewYorkCoordinates() {
    GeocodingServiceResponseDTO expected = GeocodingServiceResponseDTO.builder()
      .lat("0")
      .lon("0")
      .build();

    when(service.getCoordinatesByLocationName(any())).thenReturn(Optional.of(expected));

    Optional<GeocodingServiceResponseDTO> coords = service.getCoordinatesByLocationName("New York");
    assertEquals(coords.get(), expected);
  }

  @Test
  void testGetCoordinatesFromInvalidName() {
    String name = "ThisIsTheNameOfACityThatShould123NotExist";
    GeocodingServiceResponseDTO expected = null;

    when(service.getCoordinatesByLocationName(name)).thenReturn(null);

    assertThrows(LocationNotFoundException.class, () -> {
      service.getCoordinatesByLocationName(name);
    });
  }

  @Test
  void testGetCoordinatesFromSeparatedByCommaLocation() {
    String name = "Mexico City,Mexico";
    GeocodingServiceResponseDTO expected = GeocodingServiceResponseDTO.builder()
      .lat("19.42847")
      .lon("-99.12766")
      .zip("06080")
      .country("Mexico")
      .build();

    when(service.getCoordinatesByLocationName(any())).thenReturn(
      Optional.ofNullable(expected)
    );

    Optional<GeocodingServiceResponseDTO> res = service.getCoordinatesByLocationName(name);
    assert res.get().equals(expected);
  }

}
