package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX App.
 */
public class WarehouseApp extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Warehouse.fxml"));
    Parent parent = fxmlLoader.load();
    stage.setScene(new Scene(parent));
    stage.show();
    stage.setMinHeight(520);
    stage.setMinWidth(620);
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
