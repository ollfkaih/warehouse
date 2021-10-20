package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import core.Item;
import core.User;
import core.Warehouse;
import data.mapper.LocalDateTimeDeserializer;
import data.mapper.LocalDateTimeSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Wrapper class for static functions that serialize and deserialize Warehouse to JSON.
 */
public abstract class WarehouseSerializer {
  public static void itemsToJson(Warehouse warehouse, OutputStream outputStream)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(getLocalDateTimeModule());
    objectMapper.writeValue(outputStream, warehouse.getAllItems());
  }

  public static void usersToJson(Warehouse warehouse, OutputStream outputStream)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.writeValue(outputStream, warehouse.getUsers());
  }

  public static Warehouse jsonToWarehouse(InputStream items, InputStream users) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(getLocalDateTimeModule());

    List<Item> warehouseItems = mapper.readValue(items, new TypeReference<List<Item>>() {});
    List<User> warehouseUsers = mapper.readValue(users, new TypeReference<List<User>>() {});

    Warehouse warehouse = new Warehouse();
    for (Item item : warehouseItems) {
      warehouse.addItem(item);
    }

    for (User user : warehouseUsers) {
      warehouse.addUser(user);
    }

    return warehouse;
  }

  private static SimpleModule getLocalDateTimeModule() {
    SimpleModule localDateTimeModule = new SimpleModule();
    localDateTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    localDateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    return localDateTimeModule;
  }
}
