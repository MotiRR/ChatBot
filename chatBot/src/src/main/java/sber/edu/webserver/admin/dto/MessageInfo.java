package sber.edu.webserver.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageInfo {
  Integer id;
  Long time;
  String message;
  List<SentenceInfo> sentences;
}
