package sber.edu.webserver.community.bl;

import org.hibernate.Session;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.UserEntity;
import sber.edu.webserver.community.dto.UserInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommunityBusinessLogic {
  public List<UserInfo> getPeople() throws SQLException {
    List<UserInfo> people = new ArrayList<UserInfo>();
    try (Session session = DataBaseManager.getInstance().getSession()) {
      List<UserEntity> users = session.createQuery("FROM UserEntity user").list();
      for (UserEntity user : users) {
        UserInfo info = new UserInfo(user.getId(), user.getName() + ' ' + user.getLastName());

        if (!user.getPhoto().isEmpty()) {
          info.setPhoto(new String(user.getPhoto().get(0).getPhoto()));
        }

        people.add(info);
      }
    }
    return people;
  }

}
