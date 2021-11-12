package core;

import java.util.UUID;

/**
 * Authentication session, contains a user and an api-token.
 */
public class AuthSession {
  private final User user;
  private final String token;

  public AuthSession(User user) {
    this.user = user;
    this.token = UUID.randomUUID().toString();
  }

  public User getUser() {
    return user;
  }

  public String getToken() {
    return token;
  }

  public boolean isValid() {
    return true;
  }
}
