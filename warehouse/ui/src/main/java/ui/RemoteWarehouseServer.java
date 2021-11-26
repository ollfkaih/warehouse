package ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.client.ServerInterface;
import core.main.AuthSession;
import core.main.Item;
import core.main.LoginRequest;
import core.main.User;
import data.DataUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Connection to a remote server.
 */
public class RemoteWarehouseServer implements ServerInterface {
  private static final ObjectMapper objectMapper = DataUtils.getObjectMapper();
  private static final HttpClient client = HttpClient.newHttpClient();
  private final String baseUrl;

  public RemoteWarehouseServer(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  private String warehouseUrl(String... segments) {
    StringBuilder url = new StringBuilder(baseUrl + "/warehouse");
    for (String segment : segments) {
      url.append("/").append(segment);
    }
    return url.toString();
  }

  private <T> T readBody(String body, TypeReference<T> typeReference) {
    try {
      return objectMapper.readValue(body, typeReference);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private <T> String jsonify(T thing) {
    try {
      return objectMapper.writeValueAsString(thing);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private HttpResponse<String> errorThrower(HttpResponse<String> response) {
    if (response.statusCode() >= 200 && response.statusCode() < 300) {
      return response;
    }

    HashMap<String, String> bodyMap;
    try {
      bodyMap = readBody(response.body(), new TypeReference<>() {});
    } catch (Exception e) {
      return response;
    }
    if (bodyMap.containsKey("error")) {
      String errorMessage;
      if (bodyMap.containsKey("message")) {
        errorMessage = bodyMap.get("message");
      } else {
        errorMessage = bodyMap.get("error");
      }
      throw new ServerError(response.statusCode(), errorMessage);
    }
    return response;
  }

  @Override
  public CompletableFuture<Collection<Item>> getItems() {
    HttpRequest request = HttpRequest.newBuilder()
        .header("Accept", "application/json")
        .uri(URI.create(warehouseUrl("items")))
        .GET()
        .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(this::errorThrower)
        .thenApply(HttpResponse::body)
        .thenApply(body -> this.readBody(body, new TypeReference<>() {}));
  }

  @Override
  public CompletableFuture<Item> getItem(String id) {
    HttpRequest request = HttpRequest.newBuilder()
        .header("Accept", "application/json")
        .uri(URI.create(warehouseUrl("item", id)))
        .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(this::errorThrower)
        .thenApply(HttpResponse::body)
        .thenApply(body -> this.readBody(body, new TypeReference<>() {}));
  }

  @Override
  public CompletableFuture<Boolean> putItem(Item item, AuthSession auth) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(warehouseUrl("item", item.getId())))
        .header("Content-Type", "application/json")
        .header("auth-token", auth.getToken())
        .PUT(HttpRequest.BodyPublishers.ofString(jsonify(item)))
        .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(this::errorThrower)
        .thenApply(HttpResponse::body)
        .thenApply(body -> this.readBody(body, new TypeReference<>() {}));
  }

  @Override
  public CompletableFuture<Item> removeItem(String id, AuthSession auth) {
    HttpRequest request = HttpRequest.newBuilder()
        .header("Accept", "application/json")
        .header("auth-token", auth.getToken())
        .uri(URI.create(warehouseUrl("item", id)))
        .DELETE()
        .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(this::errorThrower)
        .thenApply(HttpResponse::body)
        .thenApply(body -> this.readBody(body, new TypeReference<>() {}));
  }

  @Override
  public CompletableFuture<AuthSession> login(LoginRequest loginRequest) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(warehouseUrl("user", "login")))
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonify(loginRequest)))
        .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(this::errorThrower)
        .thenApply(HttpResponse::body)
        .thenApply(body -> this.readBody(body, new TypeReference<>() {}));
  }

  @Override
  public CompletableFuture<Void> register(User user) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(warehouseUrl("user", "register")))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonify(user)))
        .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(this::errorThrower)
        .thenApply(a -> null);
  }
}
