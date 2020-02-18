package sber.edu.webserver.responses.exceptions;

public class UnauthorizedException extends BusinessLogicException {

  public UnauthorizedException() {
    super("Текущая сессия устарела. Авторизуйтесь заново");
  }
}
