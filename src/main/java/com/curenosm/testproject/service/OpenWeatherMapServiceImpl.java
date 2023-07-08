package com.curenosm.testproject.service;

import com.curenosm.testproject.configuration.OpenWeatherProps;
import com.curenosm.testproject.dto.GeocodingServiceResponseDTO;
import com.curenosm.testproject.dto.OneCallServiceResponseDTO;
import com.curenosm.testproject.dto.TimeMachineServiceResponseDTO;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OpenWeatherMapServiceImpl implements OpenWeatherService {

  private final RestTemplate client;
  private final OpenWeatherProps props;

  @Autowired
  public OpenWeatherMapServiceImpl (RestTemplate client, OpenWeatherProps props) {
    this.client = client;
    this.props = props;
  }

  /**
   * Method used to retrieve the coordinates according to a location.
   * @param q String representing the location to search E.g: "Mexico City,Mexico".
   * @return A map containing the latitude and longitude of the searched place.
   */
  public Optional<GeocodingServiceResponseDTO> getCoordinatesByLocationName (String q) {
    String url = props.getUrl() + "/geo/1.0/direct";
    try {

      GeocodingServiceResponseDTO res = client.getForObject(
        url,
        GeocodingServiceResponseDTO.class,
        Map.of("q", q)
      );

      return Optional.of(res);
    } catch (RestClientException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }


  /**
   * Method intended to get the weather report of a location in the current time
   *
   * @param lat Latitude of the place to report.
   * @param lon Longitude of the place to report.
   * @param units Units used to display the information.
   * @return A map containing the required info.
   */
  public Optional<OneCallServiceResponseDTO> oneCall(String lat, String lon, String units) {
    String url = props.getUrl() + "/data/3.0/onecall";

    try {
      OneCallServiceResponseDTO res = client.getForObject(url, OneCallServiceResponseDTO.class, Map.of(
        "lat", lat,
        "lon", lon,
        "units", units,
        "appid", props.getApiKey()));

      return Optional.of(res);
    } catch (RestClientException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  /**
   * Method intended to get the weather report of a location in the specified time
   *
   * @param lat Latitude of the place to report.
   * @param lon Longitude of the place to report.
   * @param time Timestamp of the moment to query.
   * @param units Units used to display the information.
   * @return A map containing the required info.
   */
  public Optional<TimeMachineServiceResponseDTO> timeMachine(String lat, String lon, String time, String units) {
    String url = props.getUrl() + "/data/3.0/onecall/timemachine";

    try {
      TimeMachineServiceResponseDTO res = client.getForObject(url,
        TimeMachineServiceResponseDTO.class,
        Map.of(
          "lat", lat,
          "lon", lon,
          "time", time,
          "units", units,
          "appid", props.getApiKey()));

      return Optional.of(res);
    } catch (RestClientException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

}
