package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.Item;
import core.Warehouse;

public class WarehouseFileSaverTest {
    @Test
    @DisplayName("Test that a Warehouse can be saved and loaded again")
    public void testWarehouseSave() throws IOException {
        Warehouse warehouse = new Warehouse();
        warehouse.addItem(new Item(0, "Bok", 4));
        warehouse.addItem(new Item(1, "Laks"));
        warehouse.addItem(new Item(43, "Kaffe", 100));

        WarehouseFileSaver fileSaver = new WarehouseFileSaver();

        fileSaver.saveWarehouse(warehouse);

        Warehouse newWarehouse = fileSaver.getWarehouse();


        assertEquals(warehouse.getAllItems().size(), newWarehouse.getAllItems().size());
        for (Item item : warehouse.getAllItems()) {
            Item newItem = newWarehouse.findItem(item.getId());
            assertEquals(item.getId(), newItem.getId());
            assertEquals(item.getName(), newItem.getName());
            assertEquals(item.getQuantity(), newItem.getQuantity());
        }
    }
}
