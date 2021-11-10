package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.CoreConst.SortOption;

public class ClientWarehouseTest {

  ServerInterface server = mock(ServerInterface.class);

  ClientWarehouse wh;
  Item item;
  Item anotherItem;
  User admin;
  User user;

  @BeforeEach
  public void setup() {
    reset(server);

    CompletableFuture<Collection<Item>> getItemsFuture = CompletableFuture.completedFuture(List.of());
    when(server.getItems()).thenReturn(getItemsFuture);

    wh = new ClientWarehouse(server);

    verify(server, times(1)).getItems();
    reset(server);

    item = new Item("itemName");
    anotherItem = new Item("secondItem");
    admin = new User("admin", "password123", true);
    user = new User("user", "superSecure", false);
  }

  @Test
  @DisplayName("Test that adding item to warehouse sends item to server and updates local items")
  public void testAddItem() {
    CompletableFuture<Collection<Item>> getItemsFuture = new CompletableFuture<>();
    when(server.getItems()).thenReturn(getItemsFuture);

    CompletableFuture<Void> addItemFuture = new CompletableFuture<>();
    when(server.addItem(item)).thenReturn(addItemFuture);

    wh.addItem(item);

    verify(server, times(1)).addItem(item);
    verify(server, times(0)).getItems();

    // item should be added locally even though the future is not completed
    assertEquals(1, wh.getAllItems().size());
    assertTrue(wh.getAllItems().contains(item));

    // complete adding item
    addItemFuture.complete(null);
    verify(server, times(1)).getItems();

    // getting from server returns a second item :O
    getItemsFuture.complete(List.of(item, anotherItem));
    assertEquals(2, wh.getAllItems().size());
    assertTrue(wh.getAllItems().contains(item));
    assertTrue(wh.getAllItems().contains(anotherItem));
  }

  @Test
  @DisplayName("Test that putting items sends updates to server and updates local items")
  public void testPutItem() {
    CompletableFuture<Collection<Item>> getItemsFuture = new CompletableFuture<>();
    when(server.getItems()).thenReturn(getItemsFuture);

    CompletableFuture<Void> addItemFuture = new CompletableFuture<>();
    when(server.addItem(any())).thenReturn(addItemFuture);

    CompletableFuture<Boolean> putItemFuture = new CompletableFuture<>();
    when(server.putItem(any())).thenReturn(putItemFuture);

    wh.putItem(item);

    verify(server, times(1)).addItem(item);

    // item should be added locally even though the future is not completed
    assertEquals(1, wh.getAllItems().size());
    assertTrue(wh.getAllItems().contains(item));

    // complete adding item
    addItemFuture.complete(null);
    verify(server, times(1)).getItems();

    getItemsFuture.complete(List.of(item));
    assertEquals(1, wh.getAllItems().size());
    assertTrue(wh.getAllItems().contains(item));

    // warehouse should put changes after update
    item.setWidth(10.0);

    verify(server, times(1)).putItem(item);

    Item itemCopy = new Item(item);
    itemCopy.setWidth(5.0);

    wh.putItem(itemCopy);

    verify(server, times(1)).putItem(itemCopy);
  }

  @Test
  @DisplayName("Test that removing as item removes on the server and updates local items")
  public void testRemoveItem() {
    CompletableFuture<Collection<Item>> getItemsFuture = new CompletableFuture<>();
    when(server.getItems()).thenReturn(getItemsFuture);

    CompletableFuture<Void> addItemFuture = new CompletableFuture<>();
    when(server.addItem(item)).thenReturn(addItemFuture);

    wh.addItem(item);

    verify(server, times(1)).addItem(item);
    verify(server, times(0)).getItems();

    // item should be added locally even though the future is not completed
    assertEquals(1, wh.getAllItems().size());
    assertTrue(wh.getAllItems().contains(item));

    // complete adding item
    addItemFuture.complete(null);
    verify(server, times(1)).getItems();

    // getting from server returns a second item :O
    getItemsFuture.complete(List.of(item, anotherItem));
    assertEquals(2, wh.getAllItems().size());
    assertTrue(wh.getAllItems().contains(item));
    assertTrue(wh.getAllItems().contains(anotherItem));
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

    CompletableFuture<Collection<Item>> getItemsFuture = CompletableFuture.completedFuture(List.of(item1, item2, item3, item4, item5, item6));
    when(server.getItems()).thenReturn(getItemsFuture);

    wh = new ClientWarehouse(server);

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
    when(server.addItem(any())).thenReturn(CompletableFuture.completedFuture(null));
    when(server.putItem(any())).thenReturn(CompletableFuture.completedFuture(null));
    when(server.removeItem(any())).thenReturn(CompletableFuture.completedFuture(null));

    EntityCollectionListener<Item> listener = new EntityCollectionListener<>() {
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

    // remove item from warehouse, and test that updates no longer fire events
    wh.removeItem(item);
    assertEquals(item, removedItem);

    updated = false;
    item.setBarcode("1739280375232");;
    assertFalse(updated);

    // test removing items listener
    wh.removeItemsListener(listener);

    updated = false;
    Item item2 = new Item("item2", 2);
    wh.addItem(item2);
    assertFalse(updated);
  }
}
