package sber.edu.kafka.messageReceiver;

import java.sql.SQLException;

public interface MessageListener {
  void messageReceived(Message message) throws SQLException;
}
