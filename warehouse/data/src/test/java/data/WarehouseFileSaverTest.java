package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.Item;
import core.User;
import core.Warehouse;

class WarehouseFileSaverTest {

  private WarehouseFileSaver fileSaver;
  private Warehouse warehouse;
  private Warehouse newWarehouse;

  @BeforeEach
  private void createWarehouse() {
    warehouse = new Warehouse();
    warehouse.addItem(new Item("Bok", 4));
    warehouse.addItem(new Item("Laks"));
    warehouse.addItem(new Item("Kaffe", 100));
    warehouse.addUser(new User("a", "a", true));
    warehouse.addUser(new User("b", "b", false));
    fileSaver = new WarehouseFileSaver("testWarehouse");
  }

  @Test
  @DisplayName("Test that a Warehouse can be saved and loaded again")
  void testWarehouseSave() throws IOException {
    fileSaver.saveItems(warehouse);
    fileSaver.saveUsers(warehouse);
    newWarehouse = fileSaver.getWarehouse();

    assertEquals(warehouse.getAllItems().size(), newWarehouse.getAllItems().size());
    for (Item item : warehouse.getAllItems()) {
      Item newItem = newWarehouse.findItem(item.getId());
      assertEquals(item.getId(), newItem.getId());
      assertEquals(item.getName(), newItem.getName());
      assertEquals(item.getAmount(), newItem.getAmount());
      assertEquals(item.getCreationDate().format(Utils.dateTimeFormatter), newItem.getCreationDate().format(Utils.dateTimeFormatter));
    }

    assertEquals(warehouse.getUsers().size(), newWarehouse.getUsers().size());
    for (User user : warehouse.getUsers()) {
      String username = user.getUserName();
      String password = user.getPassword();
      Boolean admin = user.isAdmin();
      assertTrue(newWarehouse.containsUserByUsername(username));
      assertTrue(newWarehouse.containsUser(username, password, admin));
    }
  }

  @Test
  @DisplayName("Test that warehouseSaver can be loaded properly when no items folder exists")
  void testWarehouseSaveNoItems() throws IOException{
    fileSaver.saveUsers(warehouse);
    newWarehouse = fileSaver.getWarehouse();

    assertEquals(0, newWarehouse.getAllItems().size());

    assertEquals(warehouse.getUsers().size(), newWarehouse.getUsers().size());
    for (User user : warehouse.getUsers()) {
      String username = user.getUserName();
      String password = user.getPassword();
      Boolean admin = user.isAdmin();
      assertTrue(newWarehouse.containsUserByUsername(username));
      assertTrue(newWarehouse.containsUser(username, password, admin));
    }
  }

  @Test
  @DisplayName("Test that warehouseSaver can be loaded properly when no users folder exists")
  void testWarehouseSaveNoUsers() throws IOException{
    fileSaver.saveItems(warehouse);
    newWarehouse = fileSaver.getWarehouse();

    assertEquals(warehouse.getAllItems().size(), newWarehouse.getAllItems().size());
    for (Item item : warehouse.getAllItems()) {
      Item newItem = newWarehouse.findItem(item.getId());
      assertEquals(item.getId(), newItem.getId());
      assertEquals(item.getName(), newItem.getName());
      assertEquals(item.getAmount(), newItem.getAmount());
      assertEquals(item.getCreationDate().format(Utils.dateTimeFormatter), newItem.getCreationDate().format(Utils.dateTimeFormatter));
    }

    assertEquals(0, newWarehouse.getUsers().size());
  }

  @Test
  @DisplayName("Test that warehouseSaver can be loaded properly when no items or users folder exists")
  void testWarehouseSaveNoItemsNoUsers() throws IOException {
    newWarehouse = fileSaver.getWarehouse();
    assertEquals(0, newWarehouse.getAllItems().size());
    assertEquals(0, newWarehouse.getUsers().size());
  }
  
  @AfterEach
  private void cleanUpFiles() {
    try {
      fileSaver.deleteWarehouse("items");
      fileSaver.deleteWarehouse("users");
    } catch (Exception e) {
      fail("Couldn't delete file");
    }
  }
}
