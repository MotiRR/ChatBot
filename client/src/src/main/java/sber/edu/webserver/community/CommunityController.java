package sber.edu.webserver.community;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sber.edu.session.SessionKeeper;
import sber.edu.session.info.SessionId;
import sber.edu.webserver.community.bl.CommunityBusinessLogic;
import sber.edu.webserver.community.dto.UserInfo;
import sber.edu.webserver.responses.exceptions.UnauthorizedException;
import sber.edu.webserver.responses.responses.ServerOk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
public class CommunityController {
  @GetMapping("/community/people")
  public ResponseEntity<List> getAllPeople(@CookieValue("token") SessionId token) throws UnauthorizedException, SQLException {
    int userId = SessionKeeper.getInstance().getUserId(token);

    CommunityBusinessLogic bl = new CommunityBusinessLogic();

    List<UserInfo> people =
      bl.getPeople().stream()
        .filter((e) -> e.getId() != userId)
        .collect(Collectors.toList());

    return ServerOk.ok(people);
  }

  @GetMapping("/community/music")
  public ResponseEntity<List> getMusic(@CookieValue("token") SessionId token) throws UnauthorizedException {
    int maxSongsCount = 10;

    Random generator = new Random();
    int songsCount = generator.nextInt(maxSongsCount) + 1;

    List<String> songs = new ArrayList<>();
    for (int i = 1; i <= songsCount; ++i) {
      songs.add("song number #" + i);
    }

    return ServerOk.ok(songs);
  }

  @GetMapping("/community/video")
  public ResponseEntity<List> getVideo(@CookieValue("token") SessionId token) throws UnauthorizedException {
    int maxSongsCount = 10;

    Random generator = new Random();
    int songsCount = generator.nextInt(maxSongsCount) + 1;

    List<String> video = new ArrayList<>();
    for (int i = 1; i <= songsCount; ++i) {
      video.add("video number #" + i);
    }

    return ServerOk.ok(video);
  }
}
