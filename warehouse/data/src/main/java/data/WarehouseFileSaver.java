package data;

import com.fasterxml.jackson.core.type.TypeReference;
import core.Item;
import core.User;
import core.Warehouse;

import java.io.IOException;
import java.util.Collection;


/**
 * Implementation of DataPersistence that saves warehouse to json file on disk.
 */
public class WarehouseFileSaver {
  private static final String ITEMS_FOLDERNAME = "items";
  private static final String USERS_FOLDERNAME = "users";
  private final String warehouseFileName;
  private final FileSaver<Collection<Item>> itemFileSaver;
  private final FileSaver<Collection<User>> userFileSaver;

  public WarehouseFileSaver(String filename) {
    this.warehouseFileName = filename;
    this.itemFileSaver = new FileSaver<Collection<Item>>(new TypeReference<Collection<Item>>() {}, ITEMS_FOLDERNAME);
    this.userFileSaver = new FileSaver<Collection<User>>(new TypeReference<Collection<User>>() {}, USERS_FOLDERNAME);
  }

  public Warehouse getWarehouse() throws IOException {
    Collection<Item> warehouseItems = itemFileSaver.load(warehouseFileName);
    Collection<User> warehouseUsers = userFileSaver.load(warehouseFileName);

    Warehouse warehouse = new Warehouse();
    if (warehouseItems != null) {
      for (Item item : warehouseItems) {
        warehouse.addItem(item);
      }
    }

    if (warehouseUsers != null) {
      for (User user : warehouseUsers) {
        warehouse.addUser(user);
      }
    }

    return warehouse;
  }

  public void saveItems(Warehouse warehouse) throws IOException {
    itemFileSaver.save(warehouse.getAllItems(), warehouseFileName);
  }

  public void saveUsers(Warehouse warehouse) throws IOException {
    userFileSaver.save(warehouse.getUsers(), warehouseFileName);
  }

  public void deleteWarehouse() throws IOException {
    if (userFileSaver.exists(warehouseFileName)) {
      userFileSaver.delete(warehouseFileName);
    }
    if (itemFileSaver.exists(warehouseFileName)) { 
      itemFileSaver.delete(warehouseFileName);
    }
  }
}