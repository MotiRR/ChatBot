package sber.edu.webserver.responses.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ServerError implements ServerResponce {
  public static ResponseEntity generate(String text, HttpStatus status) {
    Map<String, String> result = new HashMap();
    result.put("errorText", text);
    return new ResponseEntity(result, status);
  }

  public static ResponseEntity generateInternalError() {
    return generate("Возникла непредвиденная ошибка. Обратитесь к администратору", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
