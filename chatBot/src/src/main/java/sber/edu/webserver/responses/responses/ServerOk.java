package sber.edu.webserver.responses.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ServerOk implements ServerResponce {
  public static ResponseEntity ok() {
    return new ResponseEntity(HttpStatus.OK);
  }

  public static ResponseEntity ok(Object body) {
    return new ResponseEntity(body, HttpStatus.OK);
  }
}