package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX App.
 */
public class WarehouseApp extends Application {
  FXMLLoader fxmlLoader;
    
  @Override
  public void start(Stage stage) throws IOException {
    fxmlLoader = new FXMLLoader(this.getClass().getResource("Warehouse.fxml"));
    fxmlLoader.load();
    stage.setScene(new Scene(fxmlLoader.getRoot()));
    stage.show();
    stage.setMinHeight(380);
    stage.setMinWidth(870);
    try {
      stage.getIcons().add(new Image(WarehouseApp.class.getResourceAsStream("icon/1-rounded.png")));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("[WarehouseApp.java] Icon-image not found");
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
