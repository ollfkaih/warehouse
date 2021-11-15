package core.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.User;

import java.util.UUID;

/**
 * Authentication session, contains a user and an api-token.
 */
public class AuthSession {
  private final User user;
  private final String token;

  @JsonCreator
  public AuthSession(@JsonProperty("user") User user, @JsonProperty("token") String token) {
    this.user = user;
    this.token = token;
  }

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

  @JsonIgnore
  public boolean isValid() {
    return true;
  }
}
