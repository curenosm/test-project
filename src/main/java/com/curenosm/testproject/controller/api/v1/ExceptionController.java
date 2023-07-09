package com.curenosm.testproject.controller.api.v1;

import com.curenosm.testproject.exceptions.EmptyCitiesListException;
import com.curenosm.testproject.exceptions.LocationNotFoundException;
import com.curenosm.testproject.exceptions.TimeMachineException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class that handles every Exception thrown from the controllers.
 *
 * @version 1.0
 */
@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(value = EmptyCitiesListException.class)
  public ResponseEntity<?> noCitiesIncluded() {
    return new ResponseEntity<>("No city was included in the request.", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = LocationNotFoundException.class)
  public ResponseEntity<?> locationNotFound() {
    return new ResponseEntity<>("The location could not be found.", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = TimeMachineException.class)
  public ResponseEntity<?> errorConsultingTimeMachineService() {
    return new ResponseEntity<>(
        "There was an error while connecting to an external API.",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
