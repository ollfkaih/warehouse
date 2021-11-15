package localserver;

import com.fasterxml.jackson.core.type.TypeReference;
import core.server.AuthSession;
import core.Entity;
import core.EntityCollection;
import core.Item;
import core.server.ServerInterface;
import core.server.ServerWarehouse;
import core.User;
import data.DataPersistence;
import data.EntityCollectionAutoPersistence;
import data.FileSaver;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * An implementation of ServerInterface that uses a local WarehouseServer instance.
 */
public class LocalServer implements ServerInterface {
  private final ServerWarehouse warehouse;

  private static <T extends Entity<T>> EntityCollection<T> getEntityCollection(DataPersistence<T> dataPersistence) {
    try {
      EntityCollection<T> itemEntityCollection = new EntityCollection<>();
      itemEntityCollection.addAll(dataPersistence.loadAll());
      return itemEntityCollection;
    } catch (Exception e) {
      System.out.println("LOADING SAVED ITEMS FAILED");
      return new EntityCollection<>();
    }
  }

  public LocalServer(String folderName) {
    DataPersistence<Item> itemPersistence = new FileSaver<>(new TypeReference<>() {}, folderName + "-items");
    DataPersistence<User> userPersistence = new FileSaver<>(new TypeReference<>() {}, folderName + "-users");

    warehouse = new ServerWarehouse(getEntityCollection(itemPersistence), getEntityCollection(userPersistence));

    EntityCollectionAutoPersistence<Item> autoItemPersistence = new EntityCollectionAutoPersistence<>(itemPersistence);
    warehouse.addItemsListener(autoItemPersistence);

    EntityCollectionAutoPersistence<User> autoUserPersistence = new EntityCollectionAutoPersistence<>(userPersistence);
    warehouse.addUserListener(autoUserPersistence);
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
