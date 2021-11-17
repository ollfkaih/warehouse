package ui;

import core.ClientWarehouse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import localserver.LocalServer;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * This controller shows a separate window for selecting what server to connect to.
 */
public class ServerSelectController {
  @FXML
  TextField serverUrlField;
  @FXML
  Label errorMessageField;

  private Stage stage;

  private final WarehouseController whController;

  public ServerSelectController(WarehouseController whController) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerSelect.fxml"));
      loader.setController(this);
      Parent serverSelectRoot = loader.load();
      stage = new Stage();
      stage.setScene(new Scene(serverSelectRoot));
      stage.setTitle("Koble til server");
      stage.setResizable(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
    stage.setOnCloseRequest(event -> {
      try {
        whController.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    this.whController = whController;
  }
  
  @FXML
  private void connect() {
    String serverUrl = serverUrlField.getText();

    if (serverUrl.isEmpty()) {
      errorMessageField.setText("Du må velge en server URL før du kan koble til.");
    }

    try {
      whController.loadPersistedData(new RemoteWarehouseServer(serverUrl));
    } catch (Exception e) {
      errorMessageField.setText(e.getMessage());
      return;
    }

    hideView();
    resetView();
    whController.showView();
  }

  @FXML
  private void startLocal() {
    whController.loadPersistedData(new LocalServer("local_server"));
    whController.showView();
    hideView();
    resetView();
  }

  protected void showView() {
    stage.show();
    stage.requestFocus();
  }

  protected void hideView() {
    stage.hide();
    resetView();
  }

  private void resetView() {
    errorMessageField.setText("");
  }
}
