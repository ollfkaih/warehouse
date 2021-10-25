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
 * This controller shows a separate window for loggin in.
 */
public class LoginController {
  @FXML
  TextField usernameField;
  @FXML
  PasswordField passwordField;
  @FXML
  Label errorMessageEmptyField;
  @FXML
  Label errorMessageUserNotFound;

  private String userName;
  private String password;

  private Stage stage;
  private FXMLLoader loader;
  private Parent loginRoot;

  private RegisterController registerController;

  private WarehouseController whController;
  private Warehouse wh;
  private User currentUser;

  public LoginController(WarehouseController whController, Warehouse wh) {
    try {
      loader = new FXMLLoader(getClass().getResource("Login.fxml"));
      loader.setController(this);
      loginRoot = loader.load();
      stage = new Stage();
      stage.setScene(new Scene(loginRoot));
      stage.setTitle("Login");
      stage.setResizable(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
    stage.setOnCloseRequest(event -> {
      try {
        hideLoginView();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    registerController = new RegisterController(wh);
    this.whController = whController;
    this.wh = wh;
  }
  
  @FXML
  private void login() {
    userName = usernameField.getText().toLowerCase();
    password = User.md5Hash(passwordField.getText());
    if (!userName.equals("") && !password.equals("")) {
      if (wh.containsUser(userName, password, true)) {
        currentUser = new User(userName, password, true);
        wh.setCurrentUser(currentUser);
        whController.updateUser();
        hideLoginView();
      } else {
        errorMessageEmptyField.setText("");
        errorMessageUserNotFound.setText("Bruker finnes ikke i systemet.");
      }
    } else {
      errorMessageUserNotFound.setText("");
      errorMessageEmptyField.setText("Du må fylle ut alle feltene før du kan gå videre.");
    }
  }

  @FXML
  private void register() {
    registerController.showRegisterView();
  }

  protected void showLoginView() {
    stage.show();
    stage.requestFocus();
  }

  protected void hideLoginView() {
    stage.hide();
    errorMessageEmptyField.setText("");
    errorMessageUserNotFound.setText("");
    usernameField.setText("");
    passwordField.setText("");

  }
}
