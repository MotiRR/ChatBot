package sber.edu.webserver.profile.bl;

import org.hibernate.Session;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.GalleryEntity;
import sber.edu.database.dto.UserEntity;
import sber.edu.webserver.profile.dto.UserProfile;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ProfileBusinessLogic {
  public static UserProfile getUserProfile(int userId) throws SQLException, InternalServerErrorException {
    UserEntity user = null;
    try (Session session = DataBaseManager.getInstance().getSession()) {
      String script = "FROM UserEntity user WHERE user.id = :id";
      List<UserEntity> users = session.createQuery(script).setParameter("id", userId).list();

      if (users.size() > 1) {
        throw new InternalServerErrorException();
      }

      if (!users.isEmpty()) {
        user = users.get(0);
      }
    }

    UserProfile profile = new UserProfile();

    profile.setName(user.getName());
    profile.setSecondName(user.getSecondName());
    profile.setLastName(user.getLastName());

    profile.setAge(user.getAge());
    profile.setEmail(user.getEmail());
    profile.setPhone(user.getPhone());

    profile.setBlocked(user.isBlocked());

    if (profile.isBlocked() &&
      user.getBlockingDescription() != null) {
      profile.setBlockingDescription(Arrays.asList(user.getBlockingDescription().split(";")));
    }

    if (!user.getPhoto().isEmpty()) {
      profile.setPhoto(new String(user.getPhoto().get(0).getPhoto()));
    } else {
      profile.setPhoto(null);
    }

    return profile;
  }

  public void changeUserAvatar(int userId, byte[] base64Image) throws SQLException {
    try (Session session = DataBaseManager.getInstance().getSession()) {
      session.beginTransaction();

      String script = "FROM GalleryEntity photo WHERE photo.userId = :id";
      List<GalleryEntity> photoes = session.createQuery(script)
        .setParameter("id", userId)
        .list();

      if (photoes.size() > 1) {
        return;
      }

      GalleryEntity photo = null;
      if (!photoes.isEmpty()) {
        photo = photoes.get(0);
      } else {
        photo = new GalleryEntity();
        photo.setUserId(userId);
      }

      photo.setPhoto(base64Image);
      session.saveOrUpdate(photo);

      session.getTransaction().commit();
    }
  }
}
