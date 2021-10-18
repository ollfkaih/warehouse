package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void testContructor() {
        assertEquals("a", user1.getUserName());
        assertEquals("b", user1.getPassword());
        assertEquals(true, user1.getAdmin());
        assertEquals("c", user2.getUserName());
        assertEquals("d", user2.getPassword());
        assertEquals(false, user2.getAdmin()); 
    }

    @Test
    @DisplayName("Test")
    public void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> user3= new User("", "", true));
        assertThrows(IllegalArgumentException.class, () -> user3= new User("", "b", true));
        assertThrows(IllegalArgumentException.class, () -> user3= new User("a", "", true));
    }

    @Test
    @DisplayName("toString")
    public void testToString() {
        assertEquals("Username: " + "a" + " Password: " + "b" + " Admin: " + "true", user1.toString());
        assertEquals("Username: " + "c" + " Password: " + "d" + " Admin: " + "false", user2.toString());
    }

}
