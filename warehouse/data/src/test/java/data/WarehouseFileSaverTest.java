package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.Item;
import core.Warehouse;

public class WarehouseFileSaverTest {

  private WarehouseFileSaver fileSaver;

  @Test
  @DisplayName("Test that a Warehouse can be saved and loaded again")
  public void testWarehouseSave() throws IOException {
    Warehouse warehouse = new Warehouse();
    warehouse.addItem(new Item("Bok", 4));
    warehouse.addItem(new Item("Laks"));
    warehouse.addItem(new Item("Kaffe", 100));

    fileSaver = new WarehouseFileSaver("testWarehouse");

    fileSaver.saveWarehouse(warehouse);

    Warehouse newWarehouse = fileSaver.getWarehouse();

    assertEquals(warehouse.getAllItems().size(), newWarehouse.getAllItems().size());
    for (Item item : warehouse.getAllItems()) {
      Item newItem = newWarehouse.findItem(item.getId());
      assertEquals(item.getId(), newItem.getId());
      assertEquals("Name", newItem.getName());
      assertEquals(item.getAmount(), newItem.getAmount());
      assertEquals(item.getCreationDate().format(Utils.dateTimeFormatter), newItem.getCreationDate().format(Utils.dateTimeFormatter));
    }
  }

  @AfterEach
  private void cleanUpFiles() {
    try {
      fileSaver.deleteWarehouse();
    } catch (Exception e) {
      fail("Couldn't delete file");
    }
  }
}
