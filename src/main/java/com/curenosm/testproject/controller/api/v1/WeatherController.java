package com.curenosm.testproject.controller.api.v1;

import com.curenosm.testproject.dto.GeocodingServiceResponseDTO;
import com.curenosm.testproject.dto.OneCallServiceResponseDTO;
import com.curenosm.testproject.dto.TimeMachineServiceResponseDTO;
import com.curenosm.testproject.exceptions.EmptyCitiesListException;
import com.curenosm.testproject.exceptions.LocationNotFoundException;
import com.curenosm.testproject.exceptions.TimeMachineException;
import com.curenosm.testproject.service.OpenWeatherMapService;
import jakarta.validation.constraints.NotBlank;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles all requests related to reporting the weather and more.
 *
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class WeatherController {

  private final OpenWeatherMapService service;
  private final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

  @Autowired
  public WeatherController(OpenWeatherMapService service) {
    this.service = service;
  }

  /**
   * Get a forecast of air pollution and weather of the given list of cities.
   *
   * @param cities List
   */
  @GetMapping("/weather-data")
  public ResponseEntity<List<OneCallServiceResponseDTO>> getForecastAndAirPollution(
      @RequestParam String cities) {

    if (ObjectUtils.isEmpty(cities)) throw new EmptyCitiesListException();

    String[] citiesArr = cities.split(",");

    List<GeocodingServiceResponseDTO> res =
        Arrays.stream(citiesArr)
            .map(service::getCoordinatesByLocationName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

    if (res.size() != citiesArr.length) throw new LocationNotFoundException();

    List<OneCallServiceResponseDTO> responses =
        res.stream()
            .map(response -> service.oneCall(response.getLat(), response.getLon(), "metric"))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

    if (responses.size() != res.size()) throw new LocationNotFoundException();

    return ResponseEntity.ok(responses);
  }

  /**
   * Get a report of the past ten days of the given city's weather.
   *
   * @param city Can be more specific indicating the state and country code (separated by commas).
   */
  @GetMapping("/forecast/{city}")
  public ResponseEntity<TimeMachineServiceResponseDTO> getTenDaysForecast(
      @NotBlank @PathVariable String city) {

    if (ObjectUtils.isEmpty(city)) throw new EmptyCitiesListException();

    LocalDate from = LocalDate.now().minusDays(10);
    Optional<GeocodingServiceResponseDTO> coords = service.getCoordinatesByLocationName(city);

    if (coords.isEmpty()) throw new LocationNotFoundException();

    TimeMachineServiceResponseDTO report =
        service
            .timeMachine(
                coords.get().getLat(), coords.get().getLon(), format.format(from), "metric")
            .orElseThrow(TimeMachineException::new);

    return ResponseEntity.ok(report);
  }
}
