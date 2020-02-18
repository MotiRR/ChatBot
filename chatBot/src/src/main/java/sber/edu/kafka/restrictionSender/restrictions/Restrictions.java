package sber.edu.kafka.restrictionSender.restrictions;

import java.util.HashMap;
import java.util.Map;

public class Restrictions {
  static {
    Restrictions.restrictions = new HashMap<>();

    Restrictions.restrictions.put(RestrictionsIds.SWEAR_WORD, "Использует нецензурные выражения");
    Restrictions.restrictions.put(RestrictionsIds.XXX_CONTENT, "18+");
    Restrictions.restrictions.put(RestrictionsIds.SPAM, "Распространитель спама");
    Restrictions.restrictions.put(RestrictionsIds.SECRET_INFO_USAGE, "Пересылает кофиденциальную информацию");
  }

  public static String getTranslation(RestrictionsIds id) {
    return restrictions.get(id);
  }

  public static Map<RestrictionsIds, String> getRestrictions() {
    return restrictions;
  }

  private static Map<RestrictionsIds, String> restrictions;
}
