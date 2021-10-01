package data;

import java.time.format.DateTimeFormatter;

public abstract class Utils {
  public static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
}
