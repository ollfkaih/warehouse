package ui;

import core.ClientWarehouse;
import core.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * This controller shows a separate window for registering a user.
 */
public class RegisterController {
  @FXML TextField userNameField;
  @FXML PasswordField passwordField1;
  @FXML PasswordField passwordField2;
  @FXML Label errorMessageField;

  private static final String FILENAME = "warehouse";

  private String userName;
  private String password1;
  private String password2;

  private Stage stage;

  private final ClientWarehouse warehouse;

  public RegisterController(ClientWarehouse warehouse) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
      loader.setController(this);
      Parent loginRoot = loader.load();
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
    password1 = passwordField1.getText();
    password2 = passwordField2.getText();

    if (userName.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
      errorMessageField.setText("Passordene er ikke like.");
    }
    if (!password1.equals(password2)) {
      errorMessageField.setText("Passordene er ikke like.");
    }

    User user = new User(userName, password1, true);
    CompletableFuture<Void> registerFuture = warehouse.register(user);
    registerFuture.thenAccept(x -> {
      hideRegisterView();
    }).exceptionally(e -> {
      errorMessageField.setText(e.getCause().getMessage());
      return null;
    });
  }

  protected void showRegisterView() {
    stage.show();
    stage.requestFocus();
  }

  protected void hideRegisterView() {
    stage.hide();

    errorMessageField.setText("");
    userNameField.setText("");
    passwordField1.setText("");
    passwordField2.setText("");
  }
}
