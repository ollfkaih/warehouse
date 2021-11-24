package ui;

import core.client.ClientWarehouse;
import core.main.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * This controller shows a separate window for registering a user.
 */
public class RegisterController {
  @FXML TextField usernameField;
  @FXML PasswordField passwordField1;
  @FXML PasswordField passwordField2;
  @FXML Label errorMessageField;

  private String username;
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
    usernameField.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        register();
      }
    });
    passwordField1.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        register();
      }
    });
    passwordField2.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        register();
      }
    });
    this.warehouse = warehouse;
  }

  @FXML
  private void register() {
    username = usernameField.getText();
    password1 = passwordField1.getText();
    password2 = passwordField2.getText();

    if (username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
      errorMessageField.setText("Du må fylle ut alle feltene før du kan gå videre.");
    } else if (!password1.equals(password2)) {
      errorMessageField.setText("Passordene er ikke like.");
    } else {
      User user = new User(username, password1, false);
      CompletableFuture<Void> registerFuture = warehouse.register(user);
      registerFuture.thenAccept(x -> {
        Platform.runLater(this::hideRegisterView);
      }).exceptionally(e -> {
        e.printStackTrace();
        Platform.runLater(() -> errorMessageField.setText(e.getCause().getMessage()));
        return null;
      });
    }
  }

  protected void showRegisterView() {
    stage.show();
    stage.requestFocus();
  }

  protected void hideRegisterView() {
    stage.hide();
    usernameField.setText("");
    passwordField1.setText("");
    passwordField2.setText("");
    errorMessageField.setText("");
  }
}
