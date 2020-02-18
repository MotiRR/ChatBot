package sber.edu.webserver.admin;

import lombok.Data;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;

import java.util.List;

@Data
public class BlockUserRequest {
  private Integer userId;
  private List<RestrictionsIds> restrictionsId;
}
