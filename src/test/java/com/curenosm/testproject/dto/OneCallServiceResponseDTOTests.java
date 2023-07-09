package com.curenosm.testproject.dto;

import org.junit.jupiter.api.Test;

public class OneCallServiceResponseDTOTests {

  @Test
  void verifyToStringMethodContainsAllFields() {
    OneCallServiceResponseDTO dto = OneCallServiceResponseDTO.builder().build();
    assert dto.toString().contains("lat");
    assert dto.toString().contains("lon");
  }
}
