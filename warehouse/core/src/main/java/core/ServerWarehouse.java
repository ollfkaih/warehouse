package core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class representing a physical warehouse that contains different items in various amounts.
 */
public class ServerWarehouse extends BaseWarehouse {
  private final EntityCollection<User> userCollection;
  private final Map<String, AuthSession> authSessions;

  public ServerWarehouse() {
    userCollection = new EntityCollection<>();
    authSessions = new HashMap<>();
  }

  public ServerWarehouse(EntityCollection<Item> itemCollection, EntityCollection<User> userCollection) {
    super(itemCollection);
    this.userCollection = userCollection;
    authSessions = new HashMap<>();
  }

  public Collection<User> getUsers() {
    return userCollection.getAll();
  }

  public boolean containsUserByUsername(String username) {
    return userCollection.contains(user -> user.getUserName().equalsIgnoreCase(username));
  }

  private Optional<User> findUserByUsername(String username) {
    return userCollection.find(user -> user.getUserName().equals(username));
  }

  public void addUser(User user) {
    if (containsUserByUsername(user.getUserName())) {
      throw new IllegalArgumentException("Username already taken.");
    }
    userCollection.add(user);
  }

  public AuthSession login(String username, String password) {
    User user = findUserByUsername(username).orElseThrow(() -> new IllegalArgumentException("User does not exist"));
    if (!user.checkPassword(password)) {
      throw new IllegalArgumentException("Username or password is incorrect");
    }
    AuthSession authSession = new AuthSession(user);
    authSessions.put(authSession.getToken(), authSession);
    return authSession;
  }

  public boolean isValidAuthToken(String token) {
    AuthSession session = authSessions.get(token);
    if (session == null) {
      return false;
    }
    return session.isValid();
  }

  public void addUserListener(EntityCollectionListener<User> listener) {
    userCollection.addListener(listener);
  }

  public void removeUserListener(EntityCollectionListener<User> listener) {
    userCollection.removeListener(listener);
  }
}
