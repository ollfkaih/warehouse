package ui;

import core.User;
import core.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This controller shows a separate window for registering a user.
 */
public class RegisterController {
  @FXML
  TextField userNameField;
  @FXML
  PasswordField passwordField;
  @FXML
  Label errorMessageEmptyField;

  private String userName;
  private String password;

  private Stage stage;
  private FXMLLoader loader;
  private Parent loginRoot;

  private Warehouse warehouse;
  private User user;

  public RegisterController(Warehouse warehouse) {
    try {
      loader = new FXMLLoader(getClass().getResource("Register.fxml"));
      loader.setController(this);
      loginRoot = loader.load();
      stage = new Stage();
      stage.setScene(new Scene(loginRoot));
      stage.setTitle("Register");
      stage.setResizable(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
    stage.setOnCloseRequest(event -> {
      try {
        hideRegisterView();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    this.warehouse = warehouse;

  }

  @FXML
  private void register() {
    userName = userNameField.getText();
    password = passwordField.getText();
    if (!userName.equals("") && !password.equals("")) {
      user = new User(userName, password, true);
      warehouse.addUser(user);
      hideRegisterView();
    } else {
      errorMessageEmptyField.setText("Du må fylle ut alle feltene før du kan gå videre");
    }
  }

  protected void showRegisterView() {
    stage.show();
    stage.requestFocus();
  }

  protected void hideRegisterView() {
    stage.hide();
    errorMessageEmptyField.setText("");
    userNameField.setText("");
    passwordField.setText("");
  }
}
