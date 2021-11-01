package data;

import java.time.format.DateTimeFormatter;

/**
 * Collection of utilities for the data module.
 */
public abstract class Utils {
  public static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
}
