package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for representing one type of user.
 */
public class User extends Entity<User> {
  String userName;
  String password;
  boolean admin;

  public User(@JsonProperty("id") String id, @JsonProperty("userName") String userName, @JsonProperty("password") String password,
      @JsonProperty("admin") boolean admin) {
    super(id);
    setUserName(userName);
    this.password = password; // needs to set directly to avoid double hashing
    setAdmin(admin);
  }

  public User(String userName, String password, boolean admin) {
    super();
    setUserName(userName);
    setPassword(password);
    setAdmin(admin);
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    if (userName.equals("")) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    this.userName = userName;
    notifyUpdated();
  }
  
  public void setPassword(String password) {
    if (password.equals("")) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
    this.password = md5Hash(password);
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

  public boolean checkPassword(String password) {
    return md5Hash(password).equals(this.password);
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
    notifyUpdated();
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
    return "Username: " + userName + " Password: " + password + " Admin: " + admin;
  }

  @Override
  protected User getThis() {
    return this;
  }
}
