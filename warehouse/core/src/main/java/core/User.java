package core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class for representing one type of user.
 */
public class User {
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
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    if (password.equals("")) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
    this.password = password;
  }

  public boolean getAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  @Override
  public String toString() {
    return "Username: " + userName + " Password: " + password + " Admin: " + admin;
  }
}
