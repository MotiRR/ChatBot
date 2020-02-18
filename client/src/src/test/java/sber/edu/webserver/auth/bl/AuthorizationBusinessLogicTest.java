package sber.edu.webserver.auth.bl;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuthorizationBusinessLogicTest {
  AuthorizationBusinessLogic bl = new AuthorizationBusinessLogic();

  @Test
  public void validatePhoneNumberTest() {
    assertTrue(bl.validatePhoneNumber("8-916-001-01-00"));

    assertFalse(bl.validatePhoneNumber("8-916-01-01-00"));
    assertFalse(bl.validatePhoneNumber("8-916-01-01-00"));
    assertFalse(bl.validatePhoneNumber("8-916-001-1-00"));
    assertFalse(bl.validatePhoneNumber("8-916-001-01-0"));
    assertFalse(bl.validatePhoneNumber("8-96-001-01-00"));
    assertFalse(bl.validatePhoneNumber("-916-001-01-00"));
  }

  @Test
  public void validateEmailNumber() {
    assertTrue(bl.validateEmailNumber("ivanov@ya.ru"));
    assertTrue(bl.validateEmailNumber("Ivanov@ya.ru"));
    assertTrue(bl.validateEmailNumber("ivanov.Ilya@ya.ru"));

    assertFalse(bl.validateEmailNumber("@ya.ru"));
    assertFalse(bl.validateEmailNumber("ivanov@.ru"));
    assertFalse(bl.validateEmailNumber("ivanovya.ru"));
    assertFalse(bl.validateEmailNumber("ivanov@yaru"));
    assertFalse(bl.validateEmailNumber("ivanov@ya."));
  }
}