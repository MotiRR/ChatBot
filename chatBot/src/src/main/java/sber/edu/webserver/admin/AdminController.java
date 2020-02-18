package sber.edu.webserver.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sber.edu.kafka.restrictionSender.RestrictionEntity;
import sber.edu.kafka.restrictionSender.Sender;
import sber.edu.kafka.restrictionSender.restrictions.Restrictions;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsTypes;
import sber.edu.services.UserLockService;
import sber.edu.webserver.admin.dto.MessageInfo;
import sber.edu.webserver.admin.service.AdminControllerService;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.responses.ServerError;
import sber.edu.webserver.responses.responses.ServerOk;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    @Value("${usersKeeper.server}")
    private String server;

    @Value("${usersKeeper.url}")
    private String url;

    @Autowired
    private Sender sender;

    @Autowired
    private UserLockService userLockService;

    @GetMapping("/administration/users")
    public ResponseEntity login() throws BusinessLogicException, SQLException {
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String, Object>> users = restTemplate.getForObject(server + '/' + url, List.class);

        List<UserInfo> response = users.stream().map(
                (u) -> {
                    return new UserInfo(
                            (Integer) u.get("id"),
                            (String) u.get("name"),
                            false,
                            "");
                }).collect(Collectors.toList());

        return ServerOk.ok(response);
    }

    @GetMapping("/administration/restrictions")
    public ResponseEntity restrictions() {
        return ServerOk.ok(
                Restrictions.getRestrictions().entrySet().stream()
                        .map((r) -> {
                            Map<String, String> entity = new HashMap<>();
                            entity.put("id", r.getKey().toString());
                            entity.put("label", r.getValue());
                            return entity;
                        }).collect(Collectors.toList())
        );
    }

    @PutMapping("/administration/blockUser")
    public ResponseEntity blockUser(@RequestBody BlockUserRequest request) {
        RestrictionEntity restrictionEntity = new RestrictionEntity(
                request.getUserId(),
                RestrictionsTypes.LOCK,
                request.getRestrictionsId().stream().map(
                        (rId) -> Restrictions.getTranslation(rId)
                ).collect(Collectors.toList())
        );
        if (userLockService.createBlockUser(restrictionEntity)) {
            sender.send(restrictionEntity);
            return ServerOk.ok();
        } else return ServerError.generateInternalError();
    }

    @PutMapping("/administration/unBlockUser")
    public ResponseEntity blockUser(@RequestParam(name = "userId") Integer userId) {
        RestrictionEntity restrictionEntity = new RestrictionEntity(
                userId,
                RestrictionsTypes.UNLOCK,
                null);
        if (userLockService.updateBlockUser(restrictionEntity)) {
            sender.send(restrictionEntity);
            return ServerOk.ok();
        } else return ServerError.generateInternalError();
    }

    @GetMapping("/administration/messages")
    public ResponseEntity<MessageInfo> messages(@RequestParam(name = "userId") Integer userId) throws SQLException {
        return ServerOk.ok(AdminControllerService.getUserMessages(userId));
    }
}