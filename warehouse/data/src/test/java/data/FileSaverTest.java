package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import core.main.Item;
import core.main.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class FileSaverTest {
  @Test
  @DisplayName("Test that we can write and read items and users from file")
  void testSerializeDeserialize() throws IOException {
    Item item = new Item("Bok", 4);
    User user = new User("test87", "password", true);

    FileSaver<Item> itemSaver = new FileSaver<Item>(new TypeReference<Item>() {}, "test-items-folder");
    itemSaver.save(item, "testItem");
    FileSaver<User> userSaver = new FileSaver<User>(new TypeReference<User>() {}, "test-user-folder");
    userSaver.save(user, "testUser");

    Item loadedItem = itemSaver.load("testItem");
    User loadedUser = userSaver.load("testUser");

    assertEquals(item, loadedItem);
    assertEquals(user, loadedUser);
  }
}
