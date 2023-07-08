package com.curenosm.testproject.service;

import com.curenosm.testproject.configuration.OpenWeatherProps;
import com.curenosm.testproject.dto.GeocodingServiceResponseDTO;
import com.curenosm.testproject.dto.OneCallServiceResponseDTO;
import com.curenosm.testproject.dto.TimeMachineServiceResponseDTO;
import com.curenosm.testproject.exceptions.LocationNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class OpenWeatherMapServiceImpl implements OpenWeatherMapService {

  private final RestTemplate client;
  private final OpenWeatherProps props;
  private final ObjectMapper mapper = new ObjectMapper();
  private final HttpHeaders headers;

  @Autowired
  public OpenWeatherMapServiceImpl (
    @Qualifier("openWeatherClient") RestTemplate client, OpenWeatherProps props) {
    this.client = client;
    this.props = props;
    this.headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
  }

  /**
   * Method used to retrieve the coordinates according to a location.
   * @param q String representing the location to search E.g: "Mexico City,Mexico".
   * @return A map containing the latitude and longitude of the searched place.
   */
  public Optional<GeocodingServiceResponseDTO> getCoordinatesByLocationName (String q) {
    String url = props.getUrl() + "/geo/1.0/direct";

    try {
      HttpEntity<?> entity = new HttpEntity<>(headers);

      String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
          .queryParam("q", "{q}")
          .queryParam("appid", "{appid}")
          .encode()
          .toUriString();

      String body = client.exchange(
        urlTemplate,
        HttpMethod.GET,
        entity,
        String.class,
        Map.of(
          "q", q,
          "appid", props.getApiKey()
        )
      ).getBody();

      List<GeocodingServiceResponseDTO> res = mapper.readValue(
        body,
        new TypeReference<>() {}
      );

      if (res == null || res.isEmpty())
        throw new LocationNotFoundException();

      GeocodingServiceResponseDTO result = res.stream()
        .findFirst()
        .orElseThrow(LocationNotFoundException::new);

      return Optional.of(result);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.empty();
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
    String url = props.getSslUrl() + "/data/3.0/onecall";

    try {
      HttpEntity<?> entity = new HttpEntity<>(headers);

      String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("lat", "{lat}")
        .queryParam("lon", "{lon}")
        .queryParam("units", "{units}")
        .queryParam("appid", "{appid}")
        .encode()
        .toUriString();

      String body = client.exchange(
        urlTemplate,
        HttpMethod.GET,
        entity,
        String.class,
        Map.of(
          "lat", lat,
          "lon", lon,
          "units", units,
          "appid", props.getApiKey()
        )
      ).getBody();

      List<OneCallServiceResponseDTO> res = mapper.readValue(
        body,
        new TypeReference<>() {}
      );

      if (res == null || res.isEmpty())
        throw new LocationNotFoundException();

      OneCallServiceResponseDTO result = res.stream()
        .findFirst()
        .orElseThrow(LocationNotFoundException::new);

      return Optional.of(result);
    } catch (JsonProcessingException | RestClientException e) {
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
    String url = props.getSslUrl() + "/data/3.0/onecall/timemachine";

    try {
      HttpEntity<?> entity = new HttpEntity<>(headers);

      String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("lat", "{lat}")
        .queryParam("lon", "{lon}")
        .queryParam("units", "{units}")
        .queryParam("appid", "{appid}")
        .encode()
        .toUriString();

      String body = client.exchange(
        urlTemplate,
        HttpMethod.GET,
        entity,
        String.class,
        Map.of(
          "lat", lat,
          "lon", lon,
          "units", units,
          "appid", props.getApiKey()
        )
      ).getBody();

      List<TimeMachineServiceResponseDTO> res = mapper.readValue(body, new TypeReference<>() {});

      if (res == null || res.isEmpty())
        throw new LocationNotFoundException();

      TimeMachineServiceResponseDTO result = res.stream()
        .findFirst()
        .orElseThrow(LocationNotFoundException::new);

      return Optional.of(result);
    } catch (JsonMappingException | RestClientException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return Optional.empty();
  }

}
