package ui;

import core.Item;
import core.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This controller shows a separate window with all the properties of an item, and the possibilty to change propeties of the selected item.
 */
public class DetailsViewController {
  @FXML private ScrollPane detailsViewScrollPane;
  
  @FXML private TextField inpName;
  @FXML private TextField inpAmount;
  @FXML private Button btnIncrement;
  @FXML private Button btnDecrement;
  @FXML private Label placementSection;
  @FXML private Label placementRow;
  @FXML private Label placementShelf;
  @FXML private TextField inpOrdinaryPrice;
  @FXML private TextField inpSalesPrice;
  @FXML private TextField inpRetailerPrice;
  @FXML private ComboBox<String> comboBoxSalesTax;
  @FXML private TextField inpPlacementSection;
  @FXML private TextField inpPlacementRow;
  @FXML private TextField inpPlacementShelf;
  @FXML private TextField inpDimensionsLength;
  @FXML private TextField inpDimensionsWidth;
  @FXML private TextField inpDimensionsHeigth;
  @FXML private TextField inpWeight;
  @FXML private TextField inpBarcode;
  @FXML private Button btnSave;
  @FXML private Button btnDelete;
  @FXML private ImageView barcodeImageView;

  private FXMLLoader loader;
  private Stage stage;
  private Parent detailsRoot;

  private Item item;
  private Warehouse warehouse;
  private WarehouseController warehouseController;

  private static final int safeBoundTop = 30;
  private static final int safeBoundBottom = 75;

  public DetailsViewController(Item item, Warehouse warehouse, WarehouseController warehouseController) {
    if (item == null || warehouseController == null || warehouse == null) {
      throw new IllegalArgumentException();
    }

    this.item = item;
    this.warehouse = warehouse;
    this.warehouseController = warehouseController;

    try {
      loader = new FXMLLoader(getClass().getResource("DetailsView.fxml"));
      loader.setController(this);
      detailsRoot = loader.load();
      stage = new Stage();
      stage.setScene(new Scene(detailsRoot));
    } catch (IOException e) {
      close();
    }

    try {
      stage.getIcons().add(new Image(WarehouseApp.class.getResourceAsStream("icon/1-rounded.png")));
    } catch (Exception e) {
      e.printStackTrace();
    }

    stage.setMinWidth(450);
    stage.setHeight(Screen.getPrimary().getBounds().getHeight() - safeBoundBottom);
    stage.setY(safeBoundTop);
    stage.setTitle("Rediger: " + item.getName());
    stage.setOnCloseRequest(e -> close());
  }

  private void close() {
    warehouseController.removeDetailsViewController(item);
    stage.close();
  }

  private void ensureTextFormat() {
    numberFormat(inpAmount, true);
    numberFormat(inpOrdinaryPrice, false);
    numberFormat(inpSalesPrice, false);
    numberFormat(inpRetailerPrice, false);
    numberFormat(inpDimensionsHeigth, true);
    numberFormat(inpDimensionsLength, true);
    numberFormat(inpDimensionsWidth, true);
    numberFormat(inpWeight, false);
  }
  
  private void numberFormat(TextField textField, final boolean isInteger) {
    if (textField == null) {
      return;
    }
    textField.textProperty().addListener((obs, oldv, newv) -> {
      try {
        textField.getStyleClass().removeAll(Arrays.asList("legalInput", "illegalInput"));
        if (isInteger) {
          Integer.parseInt(textField.getText());
        } else {
          Double.parseDouble(textField.getText());
        }
        textField.getStyleClass().add("legalInput");
      } catch (NumberFormatException e) {

        textField.getStyleClass().add("illegalInput");
      }
    });
  }

  protected void requestFocus() {
    stage.requestFocus();
  }

  protected void showDetailsView() {
    stage.show();
    stage.setIconified(false);
    requestFocus();
    stage.setMaxHeight(detailsRoot.prefHeight(0));
    this.btnSave.setOnAction(e -> saveItem());
    this.btnDelete.setOnAction(e -> removeItem());
    this.update();
  }
 
  private void update() {
    updateField(inpName, item.getName());
    updateField(inpAmount, item.getAmount());

    updateField(inpOrdinaryPrice, item.getRegularPrice());
    updateField(inpSalesPrice, item.getSalePrice());
    updateField(inpRetailerPrice, item.getPurchasePrice());

    updateField(inpPlacementSection, item.getSection());
    updateField(inpPlacementRow, item.getRack());
    updateField(inpPlacementShelf, item.getShelf());

    updateField(inpDimensionsHeigth, item.getHeight());
    updateField(inpDimensionsWidth, item.getWidth());
    updateField(inpDimensionsLength, item.getLength());
  
    updateField(inpWeight, item.getWeight());
    updateField(inpBarcode, item.getBarcode());

    ensureTextFormat();
    
    if (item.getBarcode() != null) {
      generateBarcodeImage();
    }
  }

  private void updateField(TextField field, Object itemProperty) {
    field.setText(itemProperty == null ? "" : String.valueOf(itemProperty));
  }

  private void generateBarcodeImage() {
    try (InputStream imageInputStream =
             BarcodeCreator.generateBarcodeImageInputStream(item.getBarcode().substring(0, 12))) {
      Image barcodeImage = new Image(imageInputStream);
      barcodeImageView.setImage(barcodeImage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveItem() {
    item.setName(inpName.getText());
    item.setAmount(Integer.parseInt(inpAmount.getText()));
    item.setRegularPrice(Double.parseDouble(inpOrdinaryPrice.getText()));
    item.setWeight(Double.parseDouble(inpWeight.getText()));
    item.setBarcode(inpBarcode.getText());
    warehouseController.saveWarehouse();
  }

  private void removeItem() {
    warehouse.removeItem(item.getId());
    warehouseController.removeDetailsViewController(item);
    stage.close();
  }

  @FXML
  protected void decrement() {
    inpAmount.setText(String.valueOf(Integer.parseInt(inpAmount.getText()) - 1));
  }

  @FXML
  protected void increment() {
    inpAmount.setText(String.valueOf(Integer.parseInt(inpAmount.getText()) + 1));
  }
}
