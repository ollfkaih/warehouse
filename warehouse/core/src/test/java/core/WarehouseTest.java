package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.CoreConst.SortOption;

public class WarehouseTest {

  Warehouse wh;
  Item item;
  User user1;
  User user2;
  User user3;
  Collection<User> users;

  @BeforeEach
  public void setup() {
    wh = new Warehouse();
    item = new Item("itemName");
    user1 = new User("a", "b", true);
    user2 = new User("a", "c", true);
    user3 = new User("a", "b", false);
    users = new ArrayList<User>();
  }

  @Test
  @DisplayName("Test adding a new warehouse")
  public void testAddToWarehouse() {
    wh.addItem(item);
    assertEquals(item, wh.getItem(item.getId()));
    assertTrue(wh.findItemsbyName("itemName").contains(item));
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

    
    assertEquals(item, wh.getItem(item.getId()));
    assertThrows(IllegalArgumentException.class, () -> wh.getItem(item2.getId()));

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

    assertEquals(itemsPriceSortedAscending, wh.getAllItemsSorted(SortOption.PRICE, true));

    assertEquals(itemsNameSortedAscending, wh.getAllItemsSorted(SortOption.NAME, true));
    assertEquals(itemsNameSortedDescending, wh.getAllItemsSorted(SortOption.NAME, false));
    assertEquals(itemsAmountSortedAscending, wh.getAllItemsSorted(SortOption.AMOUNT, true));
    assertEquals(itemsAmountSortedDescending, wh.getAllItemsSorted(SortOption.AMOUNT, false));
    assertEquals(itemsPriceSortedDescending, wh.getAllItemsSorted(SortOption.PRICE, false));
    assertEquals(itemsWeightSortedAscending, wh.getAllItemsSorted(SortOption.WEIGHT, true));
    assertEquals(itemsWeightSortedDescending, wh.getAllItemsSorted(SortOption.WEIGHT, false));
  }

  static Item addedItem;
  static Item removedItem;
  static boolean updated = false;

  @Test
  @DisplayName("Test Warehouse listener")
  void testListener() {
    EntityCollectionListener<Item> listener = new EntityCollectionListener<Item>() {
      @Override
      public void entityAdded(Item i) {
        addedItem = i;
      }

      @Override
      public void entityUpdated(Item i) {
        updated = true;
      }

      @Override
      public void entityRemoved(Item i) {
        removedItem = i;
      }
    };

    wh.addItemsListener(listener);

    wh.addItem(item);
    assertEquals(item, addedItem);

    updated = false;
    item.setHeight(6.9);
    assertTrue(updated);
    
    wh.removeItem(item);
    assertEquals(item, removedItem);

    updated = false;
    item.setBarcode("1739280375232");;
    assertFalse(updated); // item is no longer in warehouse

    wh.removeItemsListener(listener);

    updated = false;
    Item item2 = new Item("item2", 2);
    wh.addItem(item2);
    assertFalse(updated);
  }

  @Test
  @DisplayName("Test current user")
  void testCurrentUser() {
    assertNull(wh.getCurrentUser());
    wh.addUser(user1);
    wh.setCurrentUser(user1);
    assertEquals(user1, wh.getCurrentUser());
    wh.removeCurrentUser();
    assertNull(wh.getCurrentUser());
  }

  @Test
  @DisplayName("Test current user validation")
  void testCurrentUserValidation() {
    assertThrows(IllegalArgumentException.class, () -> wh.setCurrentUser(user1));
  }

  @Test
  @DisplayName("Test user list")
  void testUsers() {
    assertTrue(wh.getUsers().isEmpty());
    wh.addUser(user1);
    users.add(user1);
    assertTrue(wh.getUsers().size() == 1);
    assertTrue(wh.getUsers().contains(user1));
    assertTrue(wh.containsUser("a", "b", true));
    assertFalse(wh.containsUser("a", "d", true));
    assertFalse(wh.containsUser("c", "b", true));
    assertFalse(wh.containsUser("a", "b", false));
    assertTrue(wh.containsUserByUsername("a"));
    assertFalse(wh.containsUserByUsername("b"));
  }

  @Test
  @DisplayName("Test user list validation")
  void testUsersValidation() {
    wh.addUser(user1);
    assertThrows(IllegalArgumentException.class, () -> wh.addUser(user1));
    assertThrows(IllegalArgumentException.class, () -> wh.addUser(user2));
    assertThrows(IllegalArgumentException.class, () -> wh.addUser(user3));
  }

}
