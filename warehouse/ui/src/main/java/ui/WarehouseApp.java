package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
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
    stage.setMinWidth(500);
    try {
      stage.getIcons().add(new Image(WarehouseApp.class.getResourceAsStream("icon/1-rounded.png")));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("[WarehouseApp.java] Icon-image not found");
    }
    stage.setOnCloseRequest(event -> {
			try {
				appExit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
  }

  public static void main(String[] args) {
    launch();
  }

  private void appExit() {
    Platform.exit();
  }
}
