package sber.edu.kafka.restrictionSender;

import lombok.AllArgsConstructor;
import lombok.Data;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsTypes;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RestrictionEntity {
  private Integer userId;
  private RestrictionsTypes type;
  private List<String> description;

  @Override
  public String toString() {
    String text = "Restriction for userId: " + userId + "\n" +
      "type: " + type + "\n";

    if (type == RestrictionsTypes.LOCK) {
      text += "description:\n" + description.stream().map(
        (e) -> "- " + e).collect(Collectors.joining("\n"));
    }
    return text;
  }
}
