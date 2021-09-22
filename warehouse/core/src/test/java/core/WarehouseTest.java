package core;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WarehouseTest {

    Warehouse wh;
    Item item;

    @BeforeEach
    public void setup() {
        wh = new Warehouse();
        item = new Item(21, "itemName");
    }

    @Test
    @DisplayName("Test adding a new warehouse")
    public void testAddToWarehouse() {
        wh.addItem(item);
        assertEquals(item, wh.findItem(item.getId()));
        assertEquals(item, wh.findItemsbyName("itemName").get(0));
    }

    @Test
    @DisplayName("Test removing items from warehouse")
    public void testRemoveFromWarehouse() {
        assertEquals(0, wh.getAllItems().size());
        wh.addItem(item);
        assertEquals(1, wh.getAllItems().size());
        Item item1 = wh.removeItem(item);
        assertEquals(0, wh.getAllItems().size()); 
        assertEquals(item1, item);
    }
}
