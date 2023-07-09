package com.curenosm.testproject.service;

import com.curenosm.testproject.dto.GeocodingServiceResponseDTO;
import com.curenosm.testproject.dto.OneCallServiceResponseDTO;
import com.curenosm.testproject.dto.TimeMachineServiceResponseDTO;
import java.util.Optional;

/** Contract for the connectivity with third party vendor. */
public interface OpenWeatherMapService {
  Optional<GeocodingServiceResponseDTO> getCoordinatesByLocationName(String q);

  Optional<OneCallServiceResponseDTO> oneCall(String lat, String lon, String units);

  Optional<TimeMachineServiceResponseDTO> timeMachine(
      String lat, String lon, String time, String units);
}
