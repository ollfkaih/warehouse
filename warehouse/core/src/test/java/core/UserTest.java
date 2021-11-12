package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setup() {
        user1 = new User("a", "b", true);
        user2 = new User("c", "d", false);
    }

    @Test
    @DisplayName("Test constructor")
    void testContructor() {
        assertEquals("a", user1.getUserName());
        assertTrue(user1.checkPassword("b"));
        assertFalse(user1.checkPassword("d"));
        assertTrue(user1.isAdmin());
        assertEquals("c", user2.getUserName());
        assertTrue(user2.checkPassword("d"));
        assertFalse(user2.checkPassword("b"));
        assertFalse(user2.isAdmin());
    }

    @Test
    @DisplayName("Test")
    void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> user3= new User("", "", true));
        assertThrows(IllegalArgumentException.class, () -> user3= new User("", "b", true));
        assertThrows(IllegalArgumentException.class, () -> user3= new User("a", "", true));
    }
}
