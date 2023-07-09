package com.curenosm.testproject.dto;

import org.junit.jupiter.api.Test;

public class TimeMachineServiceResponseDTOTests {

  @Test
  void verifyToStringMethodContainsAllFields() {
    TimeMachineServiceResponseDTO dto = new TimeMachineServiceResponseDTO();
    assert dto.toString().contains("TimeMachineServiceResponseDTO");
  }
}
