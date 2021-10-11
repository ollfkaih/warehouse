package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    assertEquals("ItemName", item.getName());
    assertEquals(10, item.getAmount());
    assertNotNull(item.getId());
    assertNull(item.getBarcode());
    assertNull(item.getBrand());
    assertNotNull(item.getCreationDate());
    assertEquals(null, item.getWeight());
    assertNull(item.getPrice());
    assertThrows(IllegalArgumentException.class, () -> new Item(null, 10));
    assertThrows(IllegalArgumentException.class, () -> new Item(null));
  }

  @Test
  @DisplayName("Test validation in setters")
  void testSetterValidation() {
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("abcabcabcabca"));
    assertThrows(IllegalArgumentException.class, () -> item.setBarcode("12345678"));
    assertThrows(IllegalArgumentException.class, () -> item.setPrice(-4.0));
    assertThrows(IllegalArgumentException.class, () -> item.setWeight(-1.0));
    assertThrows(IllegalArgumentException.class, () -> item.setAmount(-4));
    assertThrows(IllegalArgumentException.class, () -> item.setName(null));
  }

  @Test
  @DisplayName("Test setters")
  void testSetters() {
    item.setId("ItemID");
    item.setName("anotherName");
    item.setAmount(400);
    item.setBarcode("0123456789012");
    item.setBrand("brandName");
    item.setPrice(19.99);

    assertEquals("ItemID", item.getId());
    assertEquals("anotherName", item.getName());
    assertEquals(400, item.getAmount());
    assertEquals("0123456789012", item.getBarcode());
    assertEquals("brandName", item.getBrand());
    assertEquals(19.99, item.getPrice());
  }
}
