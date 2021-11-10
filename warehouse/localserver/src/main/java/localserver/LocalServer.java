package localserver;

import com.fasterxml.jackson.core.type.TypeReference;
import core.AuthSession;
import core.EntityCollection;
import core.EntityCollectionListener;
import core.Item;
import core.ServerInterface;
import core.ServerWarehouse;
import core.User;
import data.DataPersistence;
import data.FileSaver;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * An implementation of ServerInterface that uses a local WarehouseServer instance.
 */
public class LocalServer implements ServerInterface {
  private static final DataPersistence<Collection<Item>> itemPersistence = new FileSaver<>(new TypeReference<>() {}, "items");
  private static final DataPersistence<Collection<User>> userPersistence = new FileSaver<>(new TypeReference<>() {}, "users");
  private static final String ITEM_KEY_POSTFIX = "_items";
  private static final String USER_KEY_POSTFIX = "_users";
  private final String itemKey;
  private final String userKey;
  private final ServerWarehouse warehouse;

  public LocalServer(String persistenceKeyPrefix) {
    this(new ServerWarehouse(getItemCollection(persistenceKeyPrefix), getUserCollection(persistenceKeyPrefix)), persistenceKeyPrefix);
  }

  private static EntityCollection<Item> getItemCollection(String itemKeyPrefix) {
    try {
      EntityCollection<Item> itemEntityCollection = new EntityCollection<>();
      itemEntityCollection.addAll(itemPersistence.load(itemKeyPrefix + ITEM_KEY_POSTFIX));
      return itemEntityCollection;
    } catch (Exception e) {
      System.out.println("LOADING SAVED ITEMS FAILED");
      return new EntityCollection<>();
    }
  }

  private static EntityCollection<User> getUserCollection(String userKeyPrefix) {
    try {
      EntityCollection<User> userEntityCollection = new EntityCollection<>();
      userEntityCollection.addAll(userPersistence.load(userKeyPrefix + USER_KEY_POSTFIX));
      return userEntityCollection;
    } catch (Exception e) {
      System.out.println("LOADING SAVED USERS FAILED");
      return new EntityCollection<>();
    }
  }

  public LocalServer(ServerWarehouse warehouse, String persistenceKeyPrefix) {
    this.itemKey = persistenceKeyPrefix + ITEM_KEY_POSTFIX;
    this.userKey = persistenceKeyPrefix + USER_KEY_POSTFIX;
    this.warehouse = warehouse;

    warehouse.addItemsListener(new EntityCollectionListener<>() {
      @Override
      public void entityAdded(Item entity) {
        saveItems();
      }

      @Override
      public void entityUpdated(Item entity) {
        saveItems();
      }

      @Override
      public void entityRemoved(Item entity) {
        saveItems();
      }
    });

    warehouse.addUserListener(new EntityCollectionListener<>() {
      @Override
      public void entityAdded(User entity) {
        saveUsers();
      }

      @Override
      public void entityUpdated(User entity) {
        saveUsers();
      }

      @Override
      public void entityRemoved(User entity) {
        saveUsers();
      }
    });
  }

  private void saveItems() {
    try {
      itemPersistence.save(warehouse.getAllItems(), itemKey);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveUsers() {
    try {
      userPersistence.save(warehouse.getUsers(), userKey);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public CompletableFuture<Collection<Item>> getItems() {
    CompletableFuture<Collection<Item>> future = new CompletableFuture<>();
    future.complete(warehouse.getAllItems());
    return future;
  }

  @Override
  public CompletableFuture<Item> getItem(String id) {
    CompletableFuture<Item> future = new CompletableFuture<>();
    future.complete(warehouse.getItem(id));
    return future;
  }

  @Override
  public CompletableFuture<Void> addItem(Item item) {
    warehouse.addItem(item);
    return new CompletableFuture<>();
  }

  @Override
  public CompletableFuture<Boolean> putItem(Item item) {
    CompletableFuture<Boolean> future = new CompletableFuture<>();
    future.complete(warehouse.putItem(item));
    return future;
  }

  @Override
  public CompletableFuture<Item> removeItem(String id) {
    CompletableFuture<Item> future = new CompletableFuture<>();
    future.complete(warehouse.removeItem(id));
    return future;
  }

  @Override
  public CompletableFuture<AuthSession> login(String username, String password) {
    CompletableFuture<AuthSession> future = new CompletableFuture<>();
    try {
      future.complete(warehouse.login(username, password));
    } catch (Exception e) {
      future.completeExceptionally(e);
    }
    return future;
  }

  @Override
  public CompletableFuture<Void> register(User user) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    try {
      warehouse.addUser(user);
      future.complete(null);
    } catch (Exception e) {
      future.completeExceptionally(e);
    }
    return future;
  }
}
