package com.curenosm.testproject.dto;

import org.junit.jupiter.api.Test;

public class GeocodingServiceResponseDTOTests {

  @Test
  void verifyToStringMethodContainsAllFields() {
    GeocodingServiceResponseDTO dto = GeocodingServiceResponseDTO.builder()
      .build();

    assert dto.toString().contains("zip");
    assert dto.toString().contains("lat");
    assert dto.toString().contains("lon");
    assert dto.toString().contains("country");
    assert dto.toString().contains("name");
  }

}
