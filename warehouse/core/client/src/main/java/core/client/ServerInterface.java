package core.client;

import core.main.AuthSession;
import core.main.Item;
import core.main.LoginRequest;
import core.main.User;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Interface that is used to communicate between the ClientWarehouse and a ServerWarehouse,
 * either a local instance of ServerWarehouse or a remote instance through the REST API.
 * The CompleteableFuture is used because the Object might take some time to receive.
 */
public interface ServerInterface {
  CompletableFuture<Collection<Item>> getItems();

  CompletableFuture<Item> getItem(String id);

  CompletableFuture<Boolean> putItem(Item item, AuthSession auth);

  CompletableFuture<Item> removeItem(String id, AuthSession auth);

  CompletableFuture<AuthSession> login(LoginRequest loginRequest);

  CompletableFuture<Void> register(User user);
}