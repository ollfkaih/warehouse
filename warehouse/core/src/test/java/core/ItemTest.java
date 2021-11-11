package core;

import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    assertThrows(IllegalArgumentException.class, () -> item.setAmount(CoreConst.MIN_AMOUNT-1));
    assertThrows(IllegalArgumentException.class, () -> item.setAmount(CoreConst.MAX_AMOUNT+1));
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("abcabcabcabca"));
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("12345678"));
    assertThrows(IllegalArgumentException.class, () -> item.setRegularPrice(CoreConst.MIN_PRICE-1));
    assertThrows(IllegalArgumentException.class, () -> item.setSalePrice(CoreConst.MIN_PRICE-1));
    assertThrows(IllegalArgumentException.class, () -> item.setPurchasePrice(CoreConst.MIN_PRICE-1));
    assertThrows(IllegalArgumentException.class, () -> item.setSection("A".repeat(CoreConst.MAX_SECTION_LENGTH+1)));
    assertThrows(IllegalArgumentException.class, () -> item.setRow("A".repeat(CoreConst.MAX_ROW_LENGTH+1)));
    assertThrows(IllegalArgumentException.class, () -> item.setShelf("A".repeat(CoreConst.MAX_SHELF_LENGTH+1)));
    assertThrows(IllegalArgumentException.class, () -> item.setHeight(CoreConst.MIN_ITEM_DIMENSION-1));
    assertThrows(IllegalArgumentException.class, () -> item.setWidth(CoreConst.MIN_ITEM_DIMENSION-1));
    assertThrows(IllegalArgumentException.class, () -> item.setLength(CoreConst.MIN_ITEM_DIMENSION-1));
    assertThrows(IllegalArgumentException.class, () -> item.setWeight(CoreConst.MIN_WEIGHT-1));
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

  static int changeCounter;

  @Test
  @DisplayName("Test listener")
  void testListener() {
    EntityListener<Item> listener = (item) -> changeCounter++;
    item.addListener(listener);

    changeCounter = 0;
    item.setName("name");
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setAmount(2);
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setBarcode("1234567890123");
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setBrand("brand");
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setRegularPrice(100.00);
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setSalePrice(99.99);
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setPurchasePrice(1.43);
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setSection("A");
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setRow("42");
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setShelf("02");
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setHeight(10.0);
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setWidth(2.32);
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setLength(53.0);
    assertEquals(changeCounter, 1);

    changeCounter = 0;
    item.setWeight(0.012);
    assertEquals(changeCounter, 1);


    item.removeListener(listener);

    changeCounter = 0;
    item.setLength(53.0);
    assertEquals(changeCounter, 0);
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
