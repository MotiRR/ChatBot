package sber.edu.webserver.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SentenceInfo {
  private final Integer id;
  private final String sentence;

  private List<String> nouns;
  private List<String> verbs;

  private List<String> restrictions;
}
