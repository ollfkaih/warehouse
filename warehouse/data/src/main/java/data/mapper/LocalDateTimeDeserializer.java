package data.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import data.DataUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Deserializer for LocalDateTime encoded in JSON.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return LocalDateTime
        .parse(p.getValueAsString(), DataUtils.dateTimeFormatter);
  }
}
