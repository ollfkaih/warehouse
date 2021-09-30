package core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.CoreConst.SortOptions;

public class WarehouseTest {

  Warehouse wh;
  Item item;

  @BeforeEach
  public void setup() {
    wh = new Warehouse();
    item = new Item("itemName");
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

  @Test
  @DisplayName("Test sorting method")
  public void testGetAllItemsSorted() {
    Item item1 = new Item("aa");
    Item item2 = new Item("ab");
    Item item3 = new Item("ac");
    Item item4 = new Item("c");
    Item item5 = new Item("ba");
    Item item6 = new Item("bb");


    item2.incrementAmount();
    item2.incrementAmount();
    item3.incrementAmount();

    wh.addItem(item1);
    wh.addItem(item2);
    wh.addItem(item3);
    wh.addItem(item4);
    wh.addItem(item5);
    wh.addItem(item6);

    List<Item> itemsNameSorted = List.of(item1, item2, item3, item5, item6, item4);
    List<Item> itemsAmountSorted = List.of(item1, item5, item6, item4, item3, item2);
    assertEquals(itemsNameSorted, wh.getAllItemsSorted(SortOptions.Name, true));
    assertEquals(itemsAmountSorted, wh.getAllItemsSorted(SortOptions.Amount, true));
  }
}
