package sber.edu.webserver.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sber.edu.session.SessionKeeper;
import sber.edu.session.info.SessionId;
import sber.edu.webserver.auth.bl.AuthorizationBusinessLogic;
import sber.edu.webserver.auth.dto.LoginRequest;
import sber.edu.webserver.auth.dto.RegisterRequest;
import sber.edu.webserver.responses.exceptions.BadRequestException;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;
import sber.edu.webserver.responses.responses.ServerOk;

import java.sql.SQLException;

@RestController
public class AuthorizationController {
  @PutMapping("/login")
  public ResponseEntity login(@RequestBody LoginRequest loginRequest) throws BusinessLogicException, SQLException {
    if (loginRequest == null) {
      throw new BadRequestException();
    }

    AuthorizationBusinessLogic bl = new AuthorizationBusinessLogic();

    bl.validateLoginRequest(loginRequest);
    return ServerOk.ok(bl.getLoginResponse(
      SessionKeeper.getInstance().createSession(loginRequest.login, loginRequest.password)));
  }

  @PostMapping("/register")
  public ResponseEntity login(@RequestBody RegisterRequest registerRequest) throws BusinessLogicException, SQLException {
    if (registerRequest == null) {
      throw new BadRequestException();
    }

    AuthorizationBusinessLogic bl = new AuthorizationBusinessLogic();

    bl.validateRegisterRequestRequest(registerRequest);

    synchronized (this) {
      if (!bl.checkIfUserLoginUnique(registerRequest.login)) {
        throw new BusinessLogicException("Пользователь с данным логином уже зарегистрирован. Придумайте другой логин");
      }
      bl.registerUser(registerRequest);
    }
    return ServerOk.ok();
  }

  @DeleteMapping("/logout")
  public ResponseEntity logout(@CookieValue("token") SessionId token) throws SQLException, InternalServerErrorException {
    SessionKeeper.getInstance().dropSession(token);
    return ServerOk.ok();
  }
}