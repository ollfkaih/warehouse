package ui;

import core.client.ClientWarehouse;
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
 * This controller shows a separate window for loggin in.
 */
public class LoginController {
  @FXML
  TextField usernameField;
  @FXML
  PasswordField passwordField;
  @FXML
  Label errorMessageField;

  private Stage stage;

  private final RegisterController registerController;

  private final WarehouseController whController;
  private final ClientWarehouse wh;

  public LoginController(WarehouseController whController, ClientWarehouse wh) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
      loader.setController(this);
      Parent loginRoot = loader.load();
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
    usernameField.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        login();
      }
    });
    passwordField.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        login();
      }
    });
    registerController = new RegisterController(wh);
    this.whController = whController;
    this.wh = wh;
  }

  @FXML
  private void login() {
    String userName = usernameField.getText();
    String password = passwordField.getText();

    if (!userName.isEmpty() && !password.isEmpty()) {
      CompletableFuture<Void> loginFuture = wh.login(userName, password);
      loginFuture.thenApply(x -> {
        Platform.runLater(() -> {
          whController.confirmLogin();
          hideLoginView();
        });
        return null;
      }).exceptionally(e -> {
        e.printStackTrace();
        Platform.runLater(() -> errorMessageField.setText("Noe gikk galt under påloggingen. Prøv igjenn."));
        return null;
      });
    } else {
      errorMessageField.setText("Du må fylle ut alle feltene før du kan gå videre.");
    }
  }

  @FXML
  private void register() {
    registerController.showRegisterView();
    resetLoginView();
  }

  protected void showLoginView() {
    stage.show();
    stage.requestFocus();
  }

  protected void hideLoginView() {
    stage.hide();
    resetLoginView();
  }

  private void resetLoginView() {
    usernameField.setText("");
    passwordField.setText("");
    errorMessageField.setText("");
  }
}
