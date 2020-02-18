package sber.edu.webserver.responses.exceptions;

public class BadRequestException extends BusinessLogicException {
  public BadRequestException() {
    super("Возникла непредвиденная ошибка. Обратитесь к администратору");
  }
}
