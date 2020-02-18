package sber.edu.webserver.messenger;

import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sber.edu.kafka.Sender;
import sber.edu.session.SessionKeeper;
import sber.edu.session.info.SessionId;
import sber.edu.webserver.messenger.dto.ChatRequest;
import sber.edu.webserver.messenger.dto.MessageRequest;
import sber.edu.webserver.profile.bl.ProfileBusinessLogic;
import sber.edu.webserver.profile.dto.UserProfile;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;
import sber.edu.webserver.responses.exceptions.UnauthorizedException;
import sber.edu.webserver.responses.responses.ServerOk;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MessengerController {
  @Autowired
  private Sender s;

  @Value("${messagesKeeper.server}")
  private String server;

  @Value("${messagesKeeper.historyReceiverUrl}")
  private String url;

  @PostMapping(value = "/messenger/history", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity getChat(@CookieValue("token") SessionId token,
                         @RequestBody ChatRequest request) throws UnauthorizedException, IOException {
    int currentUser = SessionKeeper.getInstance().getUserId(token);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(server + "/" + url)
      .queryParam("fromUser", currentUser)
      .queryParam("toUser", request.getFriendId())
      .queryParam("fromDate", request.getFromTime());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

    HttpEntity entity = new HttpEntity(headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Message[]> chat = restTemplate.exchange(
      builder.toUriString(), HttpMethod.GET, entity, Message[].class);

    return ServerOk.ok(chat.getBody());
  }

  @PostMapping("/messenger/send/message")
  ResponseEntity proccessMessage(@CookieValue("token") SessionId token,
                                 @RequestBody MessageRequest request) throws BusinessLogicException, SQLException {
    int currentUser = SessionKeeper.getInstance().getUserId(token);
    UserProfile profile = ProfileBusinessLogic.getUserProfile(currentUser);

    if (profile.isBlocked()) {
      throw new BusinessLogicException(
        "Ваша учетная запись заблокирована.\nВозможные причины:\n" +
          profile.getBlockingDescription().stream().map(
            (e) -> "- " + e).collect(Collectors.joining("\n"))
      );
    }

    s.send(
      new Message(
        currentUser,
        request.friendId,
        MessageTypes.TEXT,
        request.message,
        request.messageTime)
    );
    return ServerOk.ok();
  }

  @PostMapping("/messenger/send/file")
  ResponseEntity proccessFile(@CookieValue("token") SessionId token,
                              @RequestBody MessageRequest request) throws UnauthorizedException {
    int currentUser = SessionKeeper.getInstance().getUserId(token);
    s.send(
      new Message(
        currentUser,
        request.friendId,
        MessageTypes.FILE,
        request.message,
        request.messageTime)
    );
    return ServerOk.ok();
  }
}
