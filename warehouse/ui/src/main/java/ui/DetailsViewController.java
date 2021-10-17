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
import ui.validators.BarcodeValidator;
import ui.validators.DoubleValidator;
import ui.validators.InputValidator;
import ui.validators.IntegerValidator;
import ui.validators.MaxLengthValidator;
import ui.validators.MinLengthValidator;
import ui.validators.NotEmptyValidatior;
import ui.validators.NotNegativeValidator;
import ui.validators.RegexValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This controller shows a separate window with all the properties of an item, and the possibilty to change propeties of the selected item.
 */
public class DetailsViewController {
  @FXML private ScrollPane detailsViewScrollPane;
  
  @FXML private TextField inpName;
  @FXML private TextField inpBrand;
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
    } catch (IOException e) {
      close();
    }
    double height = Screen.getPrimary().getBounds().getHeight() - safeBoundBottom;
    if (height > 800) {
      height = 800;
    }
    stage = new Stage();
    stage.setScene(new Scene(detailsRoot, 450, height));
    stage.setMinWidth(450);
    stage.setY(safeBoundTop);
    stage.setTitle("Rediger: " + item.getName());
    stage.setOnCloseRequest(e -> close());

    try {
      stage.getIcons().add(new Image(WarehouseApp.class.getResourceAsStream("icon/1-rounded.png")));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void close() {
    warehouseController.removeDetailsViewController(item);
    stage.close();
  }

  protected void showDetailsView() {
    stage.show();
    stage.setIconified(false);
    stage.requestFocus();
    this.update();
    addInputValidationListeners();
  }

  private void addInputValidationListeners() {
    addValidationListener(inpName, false);
    addValidationListener(inpAmount, false, new IntegerValidator(), new NotNegativeValidator());

    addValidationListener(inpOrdinaryPrice, true, new DoubleValidator(), new NotNegativeValidator());
    addValidationListener(inpSalesPrice, true, new DoubleValidator(), new NotNegativeValidator());
    addValidationListener(inpRetailerPrice, true, new DoubleValidator(), new NotNegativeValidator());

    addValidationListener(inpPlacementSection, true, new MaxLengthValidator(2));
    addValidationListener(inpPlacementRow, true, new MaxLengthValidator(2));
    addValidationListener(inpPlacementShelf, true, new MaxLengthValidator(2));

    addValidationListener(inpDimensionsHeigth, true, new DoubleValidator(), new NotNegativeValidator());
    addValidationListener(inpDimensionsLength, true, new DoubleValidator(), new NotNegativeValidator());
    addValidationListener(inpDimensionsWidth, true, new DoubleValidator(), new NotNegativeValidator());

    addValidationListener(inpWeight, true, new DoubleValidator(), new NotNegativeValidator());
    addValidationListener(inpBarcode, true, new BarcodeValidator());
  }
  
  private void addValidationListener(final TextField textField, boolean nullable, InputValidator... validators) {
    InputValidator notEmptyValidator = new NotEmptyValidatior();
    textField.textProperty().addListener((obs, oldv, value) -> {
      boolean valid;

      boolean isEmpty = !notEmptyValidator.validateInput(value);
      if (!nullable && isEmpty) {
        valid = false;
      } else if (nullable && isEmpty) {
        valid = true;
      } else {
        valid = true;

        for (InputValidator validator : validators) {
          if (!validator.validateInput(value)) {
            valid = false;
            break;
          }
        }
      }
      
      setTextFieldLegality(textField, valid);
    });
  }

  private void setTextFieldLegality(TextField textField, boolean legal) {
    textField.getStyleClass().removeAll(Arrays.asList("illegalInput"));
    if (!legal) {
      textField.getStyleClass().add("illegalInput");
    }
  }
 
  private void update() {
    updatePlacementLabels();

    updateField(inpName, item.getName());
    updateField(inpBrand, item.getBrand());
    updateField(inpAmount, item.getAmount());

    updateField(inpOrdinaryPrice, item.getRegularPrice());
    updateField(inpSalesPrice, item.getSalePrice());
    updateField(inpRetailerPrice, item.getPurchasePrice());

    updateField(inpPlacementSection, item.getSection());
    updateField(inpPlacementRow, item.getRow());
    updateField(inpPlacementShelf, item.getShelf());

    updateField(inpDimensionsHeigth, item.getHeight());
    updateField(inpDimensionsWidth, item.getWidth());
    updateField(inpDimensionsLength, item.getLength());
  
    updateField(inpWeight, item.getWeight());
    updateField(inpBarcode, item.getBarcode());
    
    if (item.getBarcode() != null) {
      generateBarcodeImage();
    }
  }

  private void updateField(TextField field, Object itemProperty) {
    field.setText(itemProperty == null ? "" : String.valueOf(itemProperty));
  }

  private void updatePlacementLabels() {
    updatePlacementLabel(placementSection, item.getSection());
    updatePlacementLabel(placementRow, item.getRow());
    updatePlacementLabel(placementShelf, item.getShelf());
  }

  private void updatePlacementLabel(Label label, String placement) {
    label.setText(placement == null ? "?" : placement);
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

  @FXML
  private void saveItem() {
    saveField(inpName, () -> item.setName(inpName.getText()));
    saveField(inpBrand, () -> item.setBrand(inpBrand.getText()));
    saveField(inpAmount, () -> item.setAmount(getIntegerFieldValue(inpAmount)));

    saveField(inpOrdinaryPrice, () -> item.setRegularPrice(getDoubleFieldValue(inpOrdinaryPrice)));
    saveField(inpSalesPrice, () -> item.setSalePrice(getDoubleFieldValue(inpSalesPrice)));
    saveField(inpRetailerPrice, () -> item.setPurchasePrice(getDoubleFieldValue(inpRetailerPrice)));

    saveField(inpPlacementSection, () -> item.setSection(inpPlacementSection.getText()));
    saveField(inpPlacementRow, () -> item.setRow(inpPlacementRow.getText()));
    saveField(inpPlacementShelf, () -> item.setShelf(inpPlacementShelf.getText()));

    saveField(inpDimensionsHeigth, () -> item.setHeight(getDoubleFieldValue(inpDimensionsHeigth)));
    saveField(inpDimensionsWidth, () -> item.setWidth(getDoubleFieldValue(inpDimensionsWidth)));
    saveField(inpDimensionsLength, () -> item.setLength(getDoubleFieldValue(inpDimensionsLength)));

    saveField(inpWeight, () -> item.setWeight(getDoubleFieldValue(inpWeight)));
    saveField(inpBarcode, () -> item.setBarcode(inpBarcode.getText()));

    warehouseController.saveWarehouse();
    update();
  }

  private Double getDoubleFieldValue(TextField textField) {
    if (textField.getText().equals("") || textField.getText() == null) {
      return null;
    } else {
      return Double.valueOf(textField.getText());
    }
  }

  private Integer getIntegerFieldValue(TextField textField) {
    if (textField.getText().equals("") || textField.getText() == null) {
      return null;
    } else {
      return Integer.valueOf(textField.getText());
    }
  }

  private interface PropertySaver {
    void save() throws Exception;
  }

  private void saveField(TextField textField, PropertySaver saver) {
    try {
      saver.save();
      setTextFieldLegality(textField, true);
    } catch (Exception e) {
      setTextFieldLegality(textField, false);
    }
  }

  @FXML
  private void removeItem() {
    warehouse.removeItem(item.getId());
    warehouseController.saveWarehouse();
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

  public String toString() {
    return ""
      + inpName.getText() + "\n"
      + inpBrand.getText() + "\n"
      + inpAmount.getText() + "\n"
      + inpOrdinaryPrice.getText() + "\n"
      + inpSalesPrice.getText() + "\n"
      + inpRetailerPrice.getText() + "\n"
      + inpPlacementSection.getText() + "\n"
      + inpPlacementRow.getText() + "\n"
      + inpPlacementShelf.getText() + "\n"
      + inpDimensionsLength.getText() + "\n"
      + inpDimensionsWidth.getText() + "\n"
      + inpDimensionsHeigth.getText() + "\n"
      + inpWeight.getText() + "\n"
      + inpBarcode.getText();
  }
}
