package localserver;


import com.fasterxml.jackson.core.type.TypeReference;
import core.client.ServerInterface;
import core.main.AuthSession;
import core.main.Entity;
import core.main.EntityCollection;
import core.main.Item;
import core.main.LoginRequest;
import core.main.User;
import core.server.ServerWarehouse;
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

  public LocalServer(String folderName) {
    this(
        new FileSaver<>(new TypeReference<>() {}, folderName + "-items"),
        new FileSaver<>(new TypeReference<>() {}, folderName + "-users")
    );
  }

  public LocalServer(DataPersistence<Item> itemPersistence, DataPersistence<User> userPersistence) {
    warehouse = new ServerWarehouse(getEntityCollection(itemPersistence), getEntityCollection(userPersistence));

    EntityCollectionAutoPersistence<Item> autoItemPersistence = new EntityCollectionAutoPersistence<>(itemPersistence);
    warehouse.addItemsListener(autoItemPersistence);

    EntityCollectionAutoPersistence<User> autoUserPersistence = new EntityCollectionAutoPersistence<>(userPersistence);
    warehouse.addUserListener(autoUserPersistence);
  }
  
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

  private void verifyAuthentication(String token) {
    if (!warehouse.isValidAuthToken(token)) {
      throw new IllegalArgumentException("Auth token is invalid");
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
  public CompletableFuture<Boolean> putItem(Item item, AuthSession auth) {
    verifyAuthentication(auth.getToken());

    CompletableFuture<Boolean> future = new CompletableFuture<>();
    future.complete(warehouse.putItem(item));
    return future;
  }

  @Override
  public CompletableFuture<Item> removeItem(String id, AuthSession auth) {
    verifyAuthentication(auth.getToken());

    CompletableFuture<Item> future = new CompletableFuture<>();
    future.complete(warehouse.removeItem(id));
    return future;
  }

  @Override
  public CompletableFuture<AuthSession> login(LoginRequest loginRequest) {
    CompletableFuture<AuthSession> future = new CompletableFuture<>();
    try {
      future.complete(warehouse.login(loginRequest.getUsername(), loginRequest.getPassword()));
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
