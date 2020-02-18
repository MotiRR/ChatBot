package sber.edu.webserver.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sber.edu.session.info.SessionId;
import sber.edu.session.SessionKeeper;
import sber.edu.webserver.profile.bl.ProfileBusinessLogic;
import sber.edu.webserver.profile.dto.UserProfile;
import sber.edu.webserver.responses.exceptions.BadRequestException;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.UnauthorizedException;
import sber.edu.webserver.responses.responses.ServerOk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProfileController {
  @GetMapping("/profile")
  public ResponseEntity getProfile(@CookieValue("token") SessionId token) throws SQLException, BusinessLogicException {
    int userId = SessionKeeper.getInstance().getUserId(token);

    ProfileBusinessLogic bl = new ProfileBusinessLogic();
    UserProfile profile = bl.getUserProfile(userId);

    if (profile == null) {
      throw new BadRequestException();
    }

    return ServerOk.ok(profile);
  }

  @PutMapping("/profile/avatar/save")
  public ResponseEntity saveAvatar(@CookieValue("token") SessionId token, @RequestBody Map body) throws UnauthorizedException, SQLException {
    int userId = SessionKeeper.getInstance().getUserId(token);
    ProfileBusinessLogic bl = new ProfileBusinessLogic();
    bl.changeUserAvatar(userId, body.get("image").toString().getBytes());
    return ServerOk.ok();
  }
}
