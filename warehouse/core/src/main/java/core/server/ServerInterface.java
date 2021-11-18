package core.server;

import core.Item;
import core.User;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Interface that is used to communicate between the ClientWarehouse and a ServerWarehouse,
 * either a local instance of ServerWarehouse or a remote instance through the REST API.
 */
public interface ServerInterface {
  CompletableFuture<Collection<Item>> getItems();

  CompletableFuture<Item> getItem(String id);

  CompletableFuture<Boolean> putItem(Item item, AuthSession auth);

  CompletableFuture<Item> removeItem(String id, AuthSession auth);

  CompletableFuture<AuthSession> login(LoginRequest loginRequest);

  CompletableFuture<Void> register(User user);
}
