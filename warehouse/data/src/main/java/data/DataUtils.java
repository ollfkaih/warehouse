package data;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import data.mapper.LocalDateTimeDeserializer;
import data.mapper.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Collection of utilities for the data module.
 */
public abstract class DataUtils {
  public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
      
  private static final ObjectMapper mapper = createObjectMapper();

  private static ObjectMapper createObjectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.registerModule(getLocalDateTimeModule());
    return om;
  }

  public static ObjectMapper getObjectMapper() {
    return mapper;
  }

  private static SimpleModule getLocalDateTimeModule() {
    SimpleModule localDateTimeModule = new SimpleModule();
    localDateTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    localDateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    return localDateTimeModule;
  }
}
