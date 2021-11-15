package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.UUID;

import core.server.AuthSession;
import core.server.ServerWarehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServerWarehouseTest {
  ServerWarehouse wh;

  static User addedUser;
  static User removedUser;
  static User updatedUser;

  @BeforeEach
  public void setup() {
    addedUser = null;
    removedUser = null;
    updatedUser = null;

    wh = new ServerWarehouse();

    EntityCollectionListener<User> userListener = new EntityCollectionListener<>() {
      @Override
      public void entityAdded(User u) {
        addedUser = u;
      }

      @Override
      public void entityUpdated(User u) {
        updatedUser = u;
      }

      @Override
      public void entityRemoved(User u) {
        removedUser = u;
      }
    };

    wh.addUserListener(userListener);
  }

  @Test
  @DisplayName("Test adding user")
  public void testAddUser() {
    String username = UUID.randomUUID().toString();

    assertNull(addedUser);
    assertEquals(0, wh.getUsers().size());
    assertFalse(wh.containsUserByUsername(username));

    User user = new User(username, "password123", true);
    wh.addUser(user);

    assertEquals(user, addedUser);
    assertTrue(wh.getUsers().contains(user));
    assertEquals(1, wh.getUsers().size());
    assertTrue(wh.containsUserByUsername(username));
  }

  @Test
  @DisplayName("Test login fails on incorrect details and works on correct details")
  public void testLogin() {
    assertThrows(IllegalArgumentException.class, () -> wh.login("test", "test"));

    String username = UUID.randomUUID().toString();
    String password = UUID.randomUUID().toString();
    User user = new User(username, password, false);
    wh.addUser(user);

    assertThrows(IllegalArgumentException.class, () -> wh.login(username+"-wrong", password+"-wrong"));
    assertThrows(IllegalArgumentException.class, () -> wh.login(username, password+"-wrong"));
    assertThrows(IllegalArgumentException.class, () -> wh.login(username+"-wrong", password));

    AuthSession loggedInSession = wh.login(username, password);
    assertEquals(user, loggedInSession.getUser());
    assertNotNull(loggedInSession.getToken());
    assertTrue(loggedInSession.isValid());

    assertTrue(wh.isValidAuthToken(loggedInSession.getToken()));
    assertFalse(wh.isValidAuthToken("123"));
  }

  @Test
  @DisplayName("Test registering user with same name fails")
  public void testRegisterSameName() {
    String username = UUID.randomUUID().toString();
    String password = UUID.randomUUID().toString();
    User user = new User(username, password, false);
    wh.addUser(user);

    User user2 = new User(username, UUID.randomUUID().toString(), true);
    assertThrows(IllegalArgumentException.class, () -> wh.addUser(user2));

    User user3 = new User(username.toUpperCase(), UUID.randomUUID().toString(), false);
    assertThrows(IllegalArgumentException.class, () -> wh.addUser(user3));
  }

  @Test
  @DisplayName("Test that the constructor taking existing item- and user collections works")
  public void testConstructingWithExistingEntityCollections() {
    EntityCollection<Item> itemCollection = new EntityCollection<>();
    EntityCollection<User> userCollection = new EntityCollection<>();

    Item item = new Item(UUID.randomUUID().toString(), new Random().nextInt(100));
    itemCollection.add(item);

    User user = new User(UUID.randomUUID().toString(), UUID.randomUUID().toString(), true);
    userCollection.add(user);

    ServerWarehouse wh = new ServerWarehouse(itemCollection, userCollection);

    assertTrue(wh.containsItem(item.getId()));
    assertTrue(wh.containsUserByUsername(user.userName));
    assertEquals(item, wh.getItem(item.getId()));
    assertEquals(itemCollection.getAll(), wh.getAllItems());
    assertEquals(userCollection.getAll(), wh.getUsers());
  }
}
