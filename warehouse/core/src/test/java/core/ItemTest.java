package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    assertNull(item.getRack());
    assertNull(item.getShelf());
    assertNull(item.getHeight());
    assertNull(item.getWidth());
    assertNull(item.getLength());
    assertNull(item.getWeight());
    assertNotNull(item.getCreationDate());

    assertThrows(IllegalArgumentException.class, () -> new Item(null, 10));
    assertThrows(IllegalArgumentException.class, () -> new Item(null));
  }

  @Test
  @DisplayName("Test validation in setters")
  void testSetterValidation() {
    assertThrows(IllegalArgumentException.class, () -> item.setId(null));
    assertThrows(IllegalArgumentException.class, () -> item.setName(null));
    assertThrows(IllegalArgumentException.class, () -> item.setAmount(CoreConst.MIN_AMOUNT-1));
    assertThrows(IllegalArgumentException.class, () -> item.setAmount(CoreConst.MAX_AMOUNT+1));
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("abcabcabcabca"));
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("12345678"));
    assertThrows(IllegalArgumentException.class, () -> item.setRegularPrice(CoreConst.MIN_PRICE-1));
    assertThrows(IllegalArgumentException.class, () -> item.setSalePrice(CoreConst.MIN_PRICE-1));
    assertThrows(IllegalArgumentException.class, () -> item.setPurchasePrice(CoreConst.MIN_PRICE-1));
    assertThrows(IllegalArgumentException.class, () -> item.setSection("A".repeat(CoreConst.MAX_SECTION_LENGTH+1)));
    assertThrows(IllegalArgumentException.class, () -> item.setRack("A".repeat(CoreConst.MAX_RACK_LENGTH+1)));
    assertThrows(IllegalArgumentException.class, () -> item.setShelf("A".repeat(CoreConst.MAX_SHELF_LENGTH+1)));
    assertThrows(IllegalArgumentException.class, () -> item.setHeight(CoreConst.MIN_ITEM_DIMENSION-1));
    assertThrows(IllegalArgumentException.class, () -> item.setWidth(CoreConst.MIN_ITEM_DIMENSION-1));
    assertThrows(IllegalArgumentException.class, () -> item.setLength(CoreConst.MIN_ITEM_DIMENSION-1));
    assertThrows(IllegalArgumentException.class, () -> item.setWeight(CoreConst.MIN_WEIGHT-1));
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

    String itemId = getRandomString();
    item.setId(itemId);
    assertEquals(itemId, item.getId());

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

    String rack = String.valueOf(rnd.nextInt(50));
    item.setRack(rack);
    assertEquals(rack, item.getRack());

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

  public static void assertItemsEqual(Item expected, Item actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getAmount(), actual.getAmount());
    assertEquals(expected.getBarcode(), actual.getBarcode());
    assertEquals(expected.getBrand(), actual.getBrand());
    assertEquals(expected.getCreationDate(), actual.getCreationDate());
    assertEquals(expected.getCurrentPrice(), actual.getCurrentPrice());
    assertEquals(expected.getSalePrice(), actual.getSalePrice());
    assertEquals(expected.getRegularPrice(), actual.getRegularPrice());
    assertEquals(expected.getPurchasePrice(), actual.getPurchasePrice());
    assertEquals(expected.getSection(), actual.getSection());
    assertEquals(expected.getRack(), actual.getRack());
    assertEquals(expected.getShelf(), actual.getShelf());
    assertEquals(expected.getHeight(), actual.getHeight());
    assertEquals(expected.getWidth(), actual.getWidth());
    assertEquals(expected.getLength(), actual.getLength());
    assertEquals(expected.getWeight(), actual.getWeight());
  }
}
