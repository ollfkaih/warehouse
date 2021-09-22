package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ItemTest {
    Item item;

    @BeforeEach
    void setup() {
        item = new Item(123,"ItemName",10, new Date());
    }

    @Test
    @DisplayName("Test Item Constructor")
    void testConstructor() {
        assertEquals("ItemName", item.getName());
        assertEquals(123, item.getId());
        assertEquals(10, item.getAmount());
        assertThrows(IllegalArgumentException.class, () -> new Item(123, null));
    }

    @Test
    @DisplayName("Test validation in setters")
    void testSetterValidation() {
        assertThrows(IllegalArgumentException.class, () -> item.setAmount(-4));
        assertThrows(IllegalArgumentException.class, () -> item.setName(null));

        item.setId(321);
        item.setName("anotherName");
        item.setAmount(400);

        assertEquals(321, item.getId());
        assertEquals("anotherName", item.getName());
        assertEquals(400, item.getAmount());
    }
}
