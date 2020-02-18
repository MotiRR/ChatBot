package integro;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sber.edu.service.MainClass;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainClass.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PagesLoadingTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeClass
  public static void prepare() {
    System.setProperty("sbergift.database.name", "integroTestDb.db");
  }

  @Test
  public void startPageTest() {
    String body = this.restTemplate.getForObject("/", String.class);
    assertTrue(body.startsWith("<!DOCTYPE html>"));
  }

  @Test
  public void loginPageTest() {
    String body = this.restTemplate.getForObject("/forms/login/login.html", String.class);
    assertTrue(body.startsWith("<!DOCTYPE html>"));
  }

  @Test
  public void registrationPageTest() {
    String body = this.restTemplate.getForObject("/forms/registration/registration.html", String.class);
    assertTrue(body.startsWith("<!DOCTYPE html>"));
  }

  @Test
  public void profilePageTest() {
    String body = this.restTemplate.getForObject("/forms/profile/profile.html", String.class);
    assertTrue(body.startsWith("<!DOCTYPE html>"));
  }
}
