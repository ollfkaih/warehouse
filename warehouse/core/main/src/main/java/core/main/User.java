package core.main;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Class for representing a user.
 */
public class User extends Entity<User> {
  private String username;
  private String password;

  public User(@JsonProperty("id") String id, @JsonProperty("username") String username, @JsonProperty("password") String password) {
    super(id);
    setUsername(username);
    setPassword(password);
  }

  public User(String username, String password, boolean hash) {
    super();
    setUsername(username);
    if (hash) {
      hashAndSetPassword(password);
    } else {
      setPassword(password);
    }
  }

  public String getUsername() {
    return username;
  }

  private void setUsername(String username) {
    if (username.equals("")) {
      throw new IllegalArgumentException("username cannot be empty");
    }
    this.username = username;
    notifyUpdated();
  }
  
  /**
   * Get the password hash. Used when serializing users for persistence.
   *
   * @return Hashed password
   */
  public String getPassword() {
    return password;
  }

  public void hashAndSetPassword(String password) {
    if (password.equals("")) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
    setPassword(md5Hash(password));
  }

  private void setPassword(String password) {
    this.password = password;
    notifyUpdated();
  }

  public boolean checkPassword(String password) {
    return md5Hash(password).equals(this.password);
  }

  private static String md5Hash(String password) {
    String outString = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(password.getBytes(StandardCharsets.UTF_8));
      byte[] bytes = md.digest();
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < bytes.length; i++) {
        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
      }
      outString = sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return outString;
  }

  @Override
  public String toString() {
    return "Username: " + username + " Password: " + password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return username.equals(user.username) && password.equals(user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }

  @Override
  protected User getThis() {
    return this;
  }
}
