package com.curenosm.testproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeocodingServiceResponseDTO {
  private String zip;
  private String name;
  private String lat;
  private String lon;
  private String country;
}
