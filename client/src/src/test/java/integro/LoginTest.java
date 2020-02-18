package integro;

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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainClass.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {
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
  public void incorrectLoginTest() {
    Map<String, String> request = new HashMap<>();
    request.put("login", "Vasiliy");
    request.put("password", "TestPass");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map> entity = new HttpEntity<Map>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange("/login", HttpMethod.PUT, entity, Map.class);

    assertEquals(400, response.getStatusCodeValue());

    assertTrue(response.getBody().containsKey("errorText"));
    assertEquals(response.getBody().get("errorText"), "Пользователь с таким логином и/или паролем не существует");
  }

  @Test
  public void emptyLoginTest() {
    Map<String, String> request = new HashMap<>();
    request.put("login", "");
    request.put("password", "TestPass");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map> entity = new HttpEntity<Map>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange("/login", HttpMethod.PUT, entity, Map.class);

    assertEquals(400, response.getStatusCodeValue());

    assertTrue(response.getBody().containsKey("errorText"));
    assertEquals(response.getBody().get("errorText"), "Логин не может быть пустым");
  }

  @Test
  public void emptyPasswordTest() {
    Map<String, String> request = new HashMap<>();
    request.put("login", "Vasya");
    request.put("password", "");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map> entity = new HttpEntity<Map>(request, headers);

    ResponseEntity<Map> response = restTemplate.exchange("/login", HttpMethod.PUT, entity, Map.class);

    assertEquals(400, response.getStatusCodeValue());

    assertTrue(response.getBody().containsKey("errorText"));
    assertEquals(response.getBody().get("errorText"), "Пароль не может быть пустым");
  }
}
