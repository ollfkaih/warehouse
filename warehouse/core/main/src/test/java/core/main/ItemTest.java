package core.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

public class ItemTest {
  Item item;

  @BeforeEach
  void setup() {
    item = new Item("ItemName", 10);
  }

  @Test
  @DisplayName("Test Item Constructor")
  void testConstructor() {
    assertNotNull(item.getId());
    assertEquals("ItemName", item.getName());
    assertEquals(10, item.getAmount());
    assertNull(item.getBarcode());
    assertNull(item.getBrand());
    assertNull(item.getRegularPrice());
    assertNull(item.getSalePrice());
    assertNull(item.getPurchasePrice());
    assertNull(item.getSection());
    assertNull(item.getRow());
    assertNull(item.getShelf());
    assertNull(item.getHeight());
    assertNull(item.getWidth());
    assertNull(item.getLength());
    assertNull(item.getWeight());
    assertNotNull(item.getCreationDate());

    assertThrows(IllegalArgumentException.class, () -> new Item(null, 10));
  }

  @Test
  @DisplayName("Test validation in setters")
  void testSetterValidation() {
    assertThrows(IllegalArgumentException.class, () -> item.setName(null));
    assertThrows(IllegalArgumentException.class, () -> item.setAmount(CoreConst.MIN_AMOUNT - 1));
    assertThrows(IllegalArgumentException.class, () -> item.setAmount(CoreConst.MAX_AMOUNT + 1));
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("abcabcabcabca"));
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("12345678"));
    assertThrows(IllegalArgumentException.class, () -> item.setRegularPrice(CoreConst.MIN_PRICE - 1));
    assertThrows(IllegalArgumentException.class, () -> item.setSalePrice(CoreConst.MIN_PRICE - 1));
    assertThrows(IllegalArgumentException.class, () -> item.setPurchasePrice(CoreConst.MIN_PRICE - 1));
    assertThrows(IllegalArgumentException.class, () -> item.setSection("A".repeat(CoreConst.MAX_POSITION_LENGTH + 1)));
    assertThrows(IllegalArgumentException.class, () -> item.setRow("A".repeat(CoreConst.MAX_POSITION_LENGTH + 1)));
    assertThrows(IllegalArgumentException.class, () -> item.setShelf("A".repeat(CoreConst.MAX_POSITION_LENGTH + 1)));
    assertThrows(IllegalArgumentException.class, () -> item.setHeight(CoreConst.MIN_ITEM_DIMENSION - 1));
    assertThrows(IllegalArgumentException.class, () -> item.setWidth(CoreConst.MIN_ITEM_DIMENSION - 1));
    assertThrows(IllegalArgumentException.class, () -> item.setLength(CoreConst.MIN_ITEM_DIMENSION - 1));
    assertThrows(IllegalArgumentException.class, () -> item.setWeight(CoreConst.MIN_WEIGHT - 1));
  }

  @Test
  @DisplayName("Test increment/decrement")
  void testIncrementDecrement() {
    item.setAmount(10);
    item.decrementAmount();
    assertEquals(9, item.getAmount());
    item.incrementAmount();
    assertEquals(10, item.getAmount());
  }

  
  @Test
  @DisplayName("Test validation in amount increment/decrement")
  void testIncrementDecrementValidation() {
    item.setAmount(CoreConst.MIN_AMOUNT);
    assertThrows(IllegalStateException.class, () -> item.decrementAmount());
    item.setAmount(CoreConst.MAX_AMOUNT);
    assertThrows(IllegalStateException.class, () -> item.incrementAmount());
  }

  @Test
  @DisplayName("Test setters")
  void testSetters() {
    Random rnd = new Random();

    String itemName = getRandomString();
    item.setName(itemName);
    assertEquals(itemName, item.getName());

    int itemAmount = rnd.nextInt(10000);
    item.setAmount(itemAmount);
    assertEquals(itemAmount, item.getAmount());

    String barcode = "0123456789012";
    item.setBarcode(barcode);
    assertEquals(barcode, item.getBarcode());

    String brandName = getRandomString();
    item.setBrand(brandName);
    assertEquals(brandName, item.getBrand());

    Double regularPrice = rnd.nextDouble();
    item.setRegularPrice(regularPrice);
    assertEquals(regularPrice, item.getRegularPrice());

    Double salePrice = rnd.nextDouble();
    item.setSalePrice(salePrice);
    assertEquals(salePrice, item.getSalePrice());

    Double purchasePrice = rnd.nextDouble();
    item.setPurchasePrice(purchasePrice);
    assertEquals(purchasePrice, item.getPurchasePrice());

    String section = "A";
    item.setSection(section);
    assertEquals(section, item.getSection());

    String row = String.valueOf(rnd.nextInt(50));
    item.setRow(row);
    assertEquals(row, item.getRow());

    String shelf = String.valueOf(rnd.nextInt(50));
    item.setShelf(shelf);
    assertEquals(shelf, item.getShelf());

    Double height = rnd.nextDouble();
    item.setHeight(height);
    assertEquals(height, item.getHeight());

    Double width = rnd.nextDouble();
    item.setWidth(width);
    assertEquals(width, item.getWidth());

    Double length = rnd.nextDouble();
    item.setLength(length);
    assertEquals(length, item.getLength());

    Double weight = rnd.nextDouble();
    item.setWeight(weight);
    assertEquals(weight, item.getWeight());
  }

  private String getRandomString() {
    return UUID.randomUUID().toString();
  }

  private void assertListenerInvoked(EntityListener<Item> mockListener) {
    verify(mockListener, times(1)).updated(item);
    clearInvocations(mockListener);
  }

  @Test
  @DisplayName("Test listener")
  void testListener() {
    EntityListener<Item> listener = (EntityListener<Item>) mock(EntityListener.class);
    doNothing().when(listener).updated(item);
    item.addListener(listener);

    item.setName("name");
    assertListenerInvoked(listener);
    
    item.setAmount(2);
    assertListenerInvoked(listener);
    
    item.setBarcode("1234567890128");
    assertListenerInvoked(listener);
    
    item.setBrand("brand");
    assertListenerInvoked(listener);
    
    item.setRegularPrice(100.00);
    assertListenerInvoked(listener);
    
    item.setSalePrice(99.99);
    assertListenerInvoked(listener);
    
    item.setPurchasePrice(1.43);
    assertListenerInvoked(listener);
    
    item.setSection("A");
    assertListenerInvoked(listener);
    
    item.setRow("42");
    assertListenerInvoked(listener);
    
    item.setShelf("02");
    assertListenerInvoked(listener);
    
    item.setHeight(10.0);
    assertListenerInvoked(listener);
    
    item.setWidth(2.32);
    assertListenerInvoked(listener);
    
    item.setLength(53.0);
    assertListenerInvoked(listener);
    
    item.setWeight(0.012);
    assertListenerInvoked(listener);

    item.removeListener(listener);

    item.setLength(53.0);
    verify(listener, never()).updated(item);
  }

  @Test
  @DisplayName("Test equals function")
  void testEquals() {
    Item item1 = new Item("ost");
    Item item2 = new Item("brød");

    assertEquals(item1, item1);
    assertNotEquals(item2, item1);
    assertNotEquals(item1, null);

    Item item1copy = new Item(item1);

    assertEquals(item1, item1copy);

    Item itemWithSameNameAs1 = new Item(item1.getName());

    assertNotEquals(item1, itemWithSameNameAs1);

    class ItemExtension extends Item {
      public ItemExtension(Item item) {
        super(item);
      }
    }

    ItemExtension itemExtension = new ItemExtension(item1);

    assertNotEquals(itemExtension, item1);
  }
}
