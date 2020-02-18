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
    System.setProperty("sberAdmin.database.name", "integroTestDb.db");
  }

  @Test
  public void startPageTest() {
    String body = this.restTemplate.getForObject("/", String.class);
    assertTrue(body.startsWith("<!DOCTYPE html>"));
  }

  @Test
  public void loginPageTest() {
    String body = this.restTemplate.getForObject("/forms/admin/admin.html", String.class);
    assertTrue(body.startsWith("<!DOCTYPE html>"));
  }
}
