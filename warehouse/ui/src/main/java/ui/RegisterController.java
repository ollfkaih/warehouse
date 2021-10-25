package ui;

import core.User;
import core.Warehouse;
import data.DataPersistence;
import data.WarehouseFileSaver;
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
  @FXML TextField userNameField;
  @FXML PasswordField passwordField;
  @FXML Label errorMessageEmptyField;
  @FXML Label errorMessageUserTaken;

  private static final String FILENAME = "warehouse";
  private final DataPersistence dataPersistence = new WarehouseFileSaver(FILENAME);

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
    userName = userNameField.getText().toLowerCase();
    password = User.md5Hash(passwordField.getText());
    if (!userName.equals("") && !password.equals("")) {
      if (warehouse.containsUserByUsername(userName)) {
        errorMessageEmptyField.setText("");
        errorMessageUserTaken.setText("Brukernavnet er allerede tatt.");
      } else {
        user = new User(userName, password, true);
        warehouse.addUser(user);
        hideRegisterView();
        saveUsers();
      }
    } else {
      errorMessageUserTaken.setText("");
      errorMessageEmptyField.setText("Du må fylle ut alle feltene før du kan gå videre.");
    }
  }

  protected void saveUsers() {
    try {
      dataPersistence.saveUsers(warehouse);
    } catch (Exception e) {
      System.out.println(e.toString());
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
