package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for representing one type of user.
 */
public class User extends Entity<User> {
  String userName;
  String password;
  boolean admin;

  public User(@JsonProperty("userName") String userName, @JsonProperty("password") String password,
      @JsonProperty("admin") boolean admin) {
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    if (password.equals("")) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
    this.password = password;
    notifyUpdated();
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
    notifyUpdated();
  }

  public static String md5Hash(String password) {
    String outString = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(password.getBytes(Charset.forName("UTF-8")));
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
