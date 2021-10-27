package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import core.Warehouse;
import data.mapper.LocalDateTimeDeserializer;
import data.mapper.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Interface for persisting warehouse data.
 */
public interface DataPersistence {
  /**
   * Get a the saved warehouse.
   *
   * @return The Warehouse
   * @throws IOException if the saved warehouse cannot be read from file
   */
  Warehouse getWarehouse() throws IOException;

  /**
   * Save the warehouse.
   *
   * @param warehouse Warehouse to be saved
   * @throws IOException If the warehouse can't be written to file
   */
  void saveItems(Warehouse warehouse) throws IOException;
  
  void saveUsers(Warehouse warehouse) throws IOException;

  static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(getLocalDateTimeModule());
    return mapper;
  }

  private static SimpleModule getLocalDateTimeModule() {
    SimpleModule localDateTimeModule = new SimpleModule();
    localDateTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    localDateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    return localDateTimeModule;
  }
}
