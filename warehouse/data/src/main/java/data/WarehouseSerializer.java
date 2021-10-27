package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Item;
import core.User;
import core.Warehouse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Wrapper class for static functions that serialize and deserialize Warehouse to JSON.
 */
public abstract class WarehouseSerializer {
  public static final ObjectMapper objectMapper = DataPersistence.createObjectMapper();

  public static void itemsToJson(Warehouse warehouse, OutputStream outputStream)
      throws IOException {
    objectMapper.writeValue(outputStream, warehouse.getAllItems());
  }

  public static void usersToJson(Warehouse warehouse, OutputStream outputStream)
      throws IOException {
    objectMapper.writeValue(outputStream, warehouse.getUsers());
  }

  public static Warehouse jsonToWarehouse(InputStream items, InputStream users) throws IOException {
    List<Item> warehouseItems = objectMapper.readValue(items, new TypeReference<List<Item>>() {});
    List<User> warehouseUsers = objectMapper.readValue(users, new TypeReference<List<User>>() {});

    Warehouse warehouse = new Warehouse();
    for (Item item : warehouseItems) {
      warehouse.addItem(item);
    }

    for (User user : warehouseUsers) {
      warehouse.addUser(user);
    }

    return warehouse;
  }
}
