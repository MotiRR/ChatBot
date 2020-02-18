package sber.edu.webserver.auth.bl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.UserAccountEntity;
import sber.edu.database.dto.UserEntity;
import sber.edu.session.info.SessionId;
import sber.edu.webserver.auth.dto.LoginRequest;
import sber.edu.webserver.auth.dto.RegisterRequest;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AuthorizationBusinessLogic {
  public boolean checkIfUserLoginUnique(String login) throws SQLException {
    try (Session session = DataBaseManager.getInstance().getSession()) {
      String script = "FROM UserAccountEntity account WHERE account.login = :login";
      List<UserAccountEntity> accounts = session.createQuery(script).setParameter("login", login).list();
      return accounts.isEmpty();
    }
  }

  public void validateLoginRequest(LoginRequest request) throws BusinessLogicException {
    if (StringUtils.isBlank(request.login)) {
      throw new BusinessLogicException("Логин не может быть пустым");
    }

    if (StringUtils.isBlank(request.password)) {
      throw new BusinessLogicException("Пароль не может быть пустым");
    }
  }

  public void validateRegisterRequestRequest(RegisterRequest request) throws BusinessLogicException {
    if (StringUtils.isBlank(request.login)) {
      throw new BusinessLogicException("Логин не может быть пустым");
    }

    if (StringUtils.isBlank(request.password)) {
      throw new BusinessLogicException("Пароль не может быть пустым");
    }

    if (StringUtils.isBlank(request.passwordRetry)) {
      throw new BusinessLogicException("Пароль для проверки не может быть пустым");
    }

    if (!request.password.equals(request.passwordRetry)) {
      throw new BusinessLogicException("Введенный пароль и пароль для подтверждения не совпадают");
    }

    if (StringUtils.isBlank(request.lastName)) {
      throw new BusinessLogicException("Фамилия не может быть пустой");
    }

    if (StringUtils.isBlank(request.name)) {
      throw new BusinessLogicException("Имя не может быть пустым");
    }

    if (StringUtils.isBlank(request.secondName)) {
      throw new BusinessLogicException("Отчество не может быть пустым");
    }

    if (StringUtils.isBlank(request.phone)) {
      throw new BusinessLogicException("Введите пожалуйста телефон");
    }

    if (!validatePhoneNumber(request.phone)) {
      throw new BusinessLogicException("Некорректный формат ввода телефона. Корректный формат 8-916-001-02-03");
    }

    if (StringUtils.isBlank(request.email)) {
      throw new BusinessLogicException("Введите пожалуйста email");
    }

    if (!validateEmailNumber(request.email)) {
      throw new BusinessLogicException("Некорректный формат ввода email. Корректный формат ivanov@ya.ru");
    }
  }

  public boolean validatePhoneNumber(String phone) {
    return Pattern.matches("\\d-\\d{3}-\\d{3}-\\d{2}-\\d{2}", phone);
  }

  public boolean validateEmailNumber(String email) {
    return Pattern.matches("\\w+[\\.\\w]*@\\w+\\.\\w+", email);
  }

  public void registerUser(RegisterRequest request) throws SQLException {
    Session session = null;
    try {
      session = DataBaseManager.getInstance().getSession();
      session.beginTransaction();

      UserAccountEntity accountEntity = new UserAccountEntity();
      accountEntity.setId(null);
      accountEntity.setLogin(request.login);
      accountEntity.setPassword(request.password);

      session.save(accountEntity);

      UserEntity userEntity = new UserEntity();
      userEntity.setId(accountEntity.getId());
      userEntity.setName(request.name);
      userEntity.setSecondName(request.secondName);
      userEntity.setLastName(request.lastName);
      userEntity.setAge((short) 12);
      userEntity.setEmail(request.email);
      userEntity.setPhone(request.phone);

      session.save(userEntity);

      session.getTransaction().commit();
    } catch (Exception ex) {
      if (session != null) {
        session.getTransaction().rollback();
      }
      throw ex;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public Map<String, String> getLoginResponse(SessionId session) {
    Map<String, String> response = new HashMap<>();
    response.put("token", session.toString());
    return response;
  }
}
