package integro;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import sber.edu.database.DataBaseManager;
import sber.edu.service.MainClass;
import sber.edu.webserver.profile.dto.UserProfile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainClass.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationAndLoginTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeClass
  public static void prepare() {
    System.setProperty("sbergift.database.name", "integroTestDb.db");
  }

  @Before
  public void clearDatabase() throws SQLException {
    DataBaseManager.getInstance().clearDataBase();
  }

  @Test
  public void registrationAndLoginTest() {
    String login = "TestLogin";
    String password = "TestPassword";

    Map<String, String> request = new HashMap<>();

    request.put("login", login);
    request.put("password", password);
    request.put("passwordRetry", password);

    request.put("name", "Ivan");
    request.put("lastName", "Ivanov");
    request.put("secondName", "Ivanovich");

    request.put("phone", "8-800-111-10-10");
    request.put("email", "ivanov@ya.ru");

    registerTestUser(request);
    String token = login(login, password);

    assertTrue(!token.isEmpty());

    UserProfile profile = loadProfile(token);

    assertTrue(profile != null);
    assertTrue(profile.getPhoto() == null);

    assertEquals(request.get("name"), profile.getName());
    assertEquals(request.get("lastName"), profile.getLastName());
    assertEquals(request.get("secondName"), profile.getSecondName());

    assertEquals(request.get("phone"), profile.getPhone());
    assertEquals(request.get("email"), profile.getEmail());
  }

  private UserProfile loadProfile(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add(HttpHeaders.COOKIE, "token=" + token);

    HttpEntity<Map> entity = new HttpEntity<Map>(headers);

    ResponseEntity<UserProfile> response = restTemplate.exchange("/profile", HttpMethod.GET, entity, UserProfile.class);
    assertEquals(200, response.getStatusCodeValue());

    return response.getBody();
  }

  private String login(String login, String password) {
    Map<String, String> request = new HashMap<>();
    request.put("login", login);
    request.put("password", password);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map> entity = new HttpEntity<Map>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange("/login", HttpMethod.PUT, entity, Map.class);

    assertEquals(200, response.getStatusCodeValue());
    assertTrue(response.getBody().containsKey("token"));

    return (String) response.getBody().get("token");
  }

  private void registerTestUser(Map<String, String> request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map> entity = new HttpEntity<Map>(request, headers);

    ResponseEntity<String> response = restTemplate.exchange("/register", HttpMethod.POST, entity, String.class);

    Assert.assertEquals(200, response.getStatusCodeValue());
  }
}
