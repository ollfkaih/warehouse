package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
    Item item2= new Item("itemName2");
    assertEquals(0, wh.getAllItems().size());
    wh.addItem(item);
    assertEquals(1, wh.getAllItems().size());
    Item item1 = wh.removeItem(item);
    assertEquals(0, wh.getAllItems().size());
    assertEquals(item1, item);
    assertThrows(IllegalArgumentException.class, () -> wh.removeItem(item2));
  }

  @Test
  @DisplayName("Test searching method")
  public void testSearchItems() {
    Item item2= new Item("itemName2");
    Item item3 = new Item("itemName");
    item.setBrand("brandName");
    item2.setBrand("brandName");
    item3.setBrand("brandName3");
    item.setAmount(10);
    item2.setAmount(20);
    item3.setAmount(30);
    List<Item> items = new ArrayList<>();
    
    wh.addItem(item);

    
    assertEquals(item, wh.findItem(item.getId()));
    assertThrows(IllegalArgumentException.class, () -> wh.findItem(item2.getId()));

    items.add(item);
    assertEquals(items, wh.findItemsbyName(item.getName()));
    items.clear();
    assertEquals(items, wh.findItemsbyName(item2.getName()));

    items.add(item);
    assertEquals(items, wh.findItemsbyBrand(item.getBrand()));
    items.clear();
    assertEquals(items, wh.findItemsbyBrand(item3.getBrand()));
    
    items.add(item);
    assertEquals(items, wh.findItemsWithAmountLessThan(15));
    items.clear();
    items.add(item3);
    wh.addItem(item3);
    assertEquals(items, wh.findItemsWithAmountMoreThan(25));
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

    item1.setRegularPrice(20.0);
    item3.setRegularPrice(30.0);
    item5.setRegularPrice(5.0);
    item6.setRegularPrice(15.0);

    item2.setWeight(10.0);
    item3.setWeight(40.0);
    item5.setWeight(5.0);
    item6.setWeight(30.0);

    wh.addItem(item1);
    wh.addItem(item2);
    wh.addItem(item3);
    wh.addItem(item4);
    wh.addItem(item5);
    wh.addItem(item6);

    List<Item> itemsNameSortedAscending = List.of(item1, item2, item3, item5, item6, item4);
    List<Item> itemsNameSortedDescending = List.of(item4, item6, item5, item3, item2, item1);
    
    List<Item> itemsAmountSortedAscending = List.of(item1, item5, item6, item4, item3, item2);
    List<Item> itemsAmountSortedDescending = List.of(item2, item3, item4, item6, item5, item1);
    
    List<Item> itemsPriceSortedAscending = List.of(item5, item6, item1, item3, item2, item4);
    List<Item> itemsPriceSortedDescending = List.of(item4, item2, item3, item1, item6, item5);
    
    List<Item> itemsWeightSortedAscending = List.of(item5, item2, item6, item3, item1, item4);
    List<Item> itemsWeightSortedDescending = List.of(item4, item1, item3, item6, item2, item5);

    assertEquals(itemsPriceSortedAscending, wh.getAllItemsSorted(SortOptions.Price, true));

    assertEquals(itemsNameSortedAscending, wh.getAllItemsSorted(SortOptions.Name, true));
    assertEquals(itemsNameSortedDescending, wh.getAllItemsSorted(SortOptions.Name, false));
    assertEquals(itemsAmountSortedAscending, wh.getAllItemsSorted(SortOptions.Amount, true));
    assertEquals(itemsAmountSortedDescending, wh.getAllItemsSorted(SortOptions.Amount, false));
    assertEquals(itemsPriceSortedDescending, wh.getAllItemsSorted(SortOptions.Price, false));
    assertEquals(itemsWeightSortedAscending, wh.getAllItemsSorted(SortOptions.Weight, true));
    assertEquals(itemsWeightSortedDescending, wh.getAllItemsSorted(SortOptions.Weight, false));
  }

  static Item addedItem;
  static Item removedItem;
  static boolean updated = false;

  @Test
  @DisplayName("Test Warehouse listener")
  public void testListener() {
    WarehouseListener listener = new WarehouseListener(){
      @Override
      public void itemAddedToWarehouse(Item i) {
        addedItem = i;
      }

      @Override
      public void warehouseItemsUpdated() {
        updated = true;
      }

      @Override
      public void itemRemovedFromWarehouse(Item i) {
        removedItem = i;
      }
    };

    wh.addListener(listener);

    wh.addItem(item);
    assertEquals(item, addedItem);
    assertTrue(updated);

    updated = false;
    item.setHeight(6.9);
    assertTrue(updated);
    
    updated = false;
    wh.removeItem(item);
    assertEquals(item, removedItem);
    assertTrue(updated);

    updated = false;
    item.setBarcode("1739280375232");;
    assertFalse(updated); // item is no longer in warehouse

    wh.removeListener(listener);

    updated = false;
    Item item2 = new Item("item2", 2);
    wh.addItem(item2);
    assertFalse(updated);
  }
}
