package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * JavaFX App.
 */
public class WarehouseApp extends Application {
  private FXMLLoader fxmlLoader;
  private WarehouseController warehouseController;
    
  @Override
  public void start(Stage stage) throws IOException {
    fxmlLoader = new FXMLLoader(this.getClass().getResource("Warehouse.fxml"));
    fxmlLoader.load();
    stage.setScene(new Scene(fxmlLoader.getRoot()));
    stage.show();
    stage.setMinHeight(380);
    stage.setMinWidth(500);
    try {
      stage.getIcons().add(new Image(WarehouseApp.class.getResourceAsStream("appIcon/1-rounded.png")));
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: [WarehouseApp.java] Icon-image not found");
    }
    warehouseController = (WarehouseController) fxmlLoader.getController();
    warehouseController.setStage(stage);
    warehouseController.hideView();
    stage.setOnCloseRequest(this::handleCloseRequest);
  }

  private void handleCloseRequest(WindowEvent event) {
    try {
      if (warehouseController.canExit()) {
        appExit();
      } else {
        closeAttemptWithChangesAlert();  
        event.consume();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    warehouseController.endExecutor();
  }

  public static void main(String[] args) {
    launch();
  }

  private void appExit() {
    Platform.exit();
  }

  private void closeAttemptWithChangesAlert() {
    Alert promptCloseConfirmationAlert = new Alert(AlertType.WARNING);
    promptCloseConfirmationAlert.setTitle("Lukk program");
    promptCloseConfirmationAlert.setHeaderText("Vil du virkelig lukke programmet?");
    promptCloseConfirmationAlert.setContentText("Eventuelle endringer i dine redigeringsvinduer vil ikke bli lagret.");
    promptCloseConfirmationAlert.initStyle(StageStyle.UTILITY);
    ButtonType dontCloseButtonType = new ButtonType("Ikke lukk", ButtonData.CANCEL_CLOSE);
    ButtonType confirmCloseButtonType = new ButtonType("Lukk", ButtonData.OK_DONE);
    
    promptCloseConfirmationAlert.getButtonTypes().setAll(dontCloseButtonType, confirmCloseButtonType);

    promptCloseConfirmationAlert.showAndWait()
    .filter(response -> response == confirmCloseButtonType)
        .ifPresent(response -> appExit());
  }
}
