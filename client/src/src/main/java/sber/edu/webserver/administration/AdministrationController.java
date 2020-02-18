package sber.edu.webserver.administration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sber.edu.webserver.community.bl.CommunityBusinessLogic;
import sber.edu.webserver.responses.responses.ServerOk;

import java.sql.SQLException;
import java.util.List;

@RestController
public class AdministrationController {
  @GetMapping("/administration/users")
  public ResponseEntity<List> getAllPeople() throws SQLException {
    CommunityBusinessLogic bl = new CommunityBusinessLogic();
    return ServerOk.ok(bl.getPeople());
  }
}