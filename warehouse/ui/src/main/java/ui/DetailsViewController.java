package ui;

import static java.util.Map.entry;

import core.client.ClientWarehouse;
import core.main.CoreConst;
import core.main.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.itemfield.ItemField;
import ui.validators.BarcodeValidator;
import ui.validators.DoubleValidator;
import ui.validators.IntegerValidator;
import ui.validators.MaxLengthValidator;
import ui.validators.MaxValueValidator;
import ui.validators.NotEmptyValidator;
import ui.validators.NotNegativeValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This controller shows a separate window (called the DetailsView of an Item)
 * with ability to view and edit the Items properties.
 */
public class DetailsViewController {
  private static final int SAFEBOUND_TOP = 30;
  private static final int SAFEBOUND_BOTTOM = 75;
  private static final int placementMaxLength = CoreConst.MAX_POSITION_LENGTH;

  @FXML private ScrollPane detailsViewScrollPane;

  @FXML private Label sharedErrorLabel;
  @FXML private TextField nameInput;
  @FXML private Label nameErrorLabel;
  @FXML private TextField brandInput;
  @FXML private TextField amountInput;
  @FXML private Button incrementButton;
  @FXML private Button decrementButton;
  @FXML private TextField placementSectionInput;
  @FXML private TextField placementRowInput;
  @FXML private TextField placementShelfInput;
  @FXML private TextField ordinaryPriceInput;
  @FXML private TextField salesPriceInput;
  @FXML private TextField retailerPriceInput;
  @FXML private TextField dimensionsLengthInput;
  @FXML private TextField dimensionsWidthInput;
  @FXML private TextField dimensionsHeightInput;
  @FXML private TextField weightInput;
  @FXML private TextField barcodeInput;
  @FXML private Button editButton;
  @FXML private Button saveButton;
  @FXML private Button deleteButton;
  @FXML private ImageView barcodeImageView;
  @FXML private HBox sectionSaveDelete;
  @FXML private Label barcodeErrorLabel;
  @FXML private Label notifyCannotEdit;

  private final Stage stage;
  private Parent detailsRoot;

  private final Item item;
  private final ClientWarehouse warehouse;
  private final WarehouseController warehouseController;

  private boolean editing = false;

  private enum Field {
    NAME, BRAND, AMOUNT, REGULAR_PRICE, SALE_PRICE, PURCHASE_PRICE, SECTION, ROW, SHELF, HEIGHT, WIDTH, LENGTH, WEIGHT,
    BARCODE
  }

  private Map<Field, ItemField> fields;

  public DetailsViewController(Item item, ClientWarehouse warehouse, WarehouseController warehouseController) {
    if (item == null || warehouseController == null || warehouse == null) {
      throw new IllegalArgumentException();
    }

    this.item = item;
    this.warehouse = warehouse;
    this.warehouseController = warehouseController;

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsView.fxml"));
      loader.setController(this);
      detailsRoot = loader.load();
    } catch (IOException e) {
      close();
    }
    double height = Screen.getPrimary().getBounds().getHeight() - SAFEBOUND_BOTTOM;
    if (height > 740) {
      height = 740;
    }
    stage = new Stage();
    stage.setScene(new Scene(detailsRoot, 450, height));
    stage.setMinWidth(450);
    stage.setY(SAFEBOUND_TOP);
    stage.setTitle("Rediger: " + item.getName());
    stage.setOnCloseRequest(e -> close());

    try {
      stage.getIcons().add(new Image(WarehouseApp.class.getResourceAsStream("appIcon/1-rounded.png")));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // toggles between info-text and edit-button
    editButton.setVisible(!(warehouse.getCurrentUser() == null));
    notifyCannotEdit.setVisible(warehouse.getCurrentUser() == null);

    maxCharsLimiter(placementSectionInput, placementMaxLength);
    maxCharsLimiter(placementRowInput, placementMaxLength);
    maxCharsLimiter(placementShelfInput, placementMaxLength);
    onlyIntLimiter(amountInput);
    onlyFloatLimiter(ordinaryPriceInput);
    onlyFloatLimiter(salesPriceInput);
    onlyFloatLimiter(retailerPriceInput);
    onlyFloatLimiter(dimensionsHeightInput);
    onlyFloatLimiter(dimensionsLengthInput);
    onlyFloatLimiter(dimensionsWidthInput);
    onlyFloatLimiter(weightInput);
    maxCharsLimiter(barcodeInput, CoreConst.MAX_BARCODE_LENGTH);
    onlyIntLimiter(barcodeInput);
  }
  
  private static void onlyIntLimiter(TextField textField) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        textField.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });
  }

  private static void onlyFloatLimiter(TextField textField) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("[0-9]*\\.?[0-9]*")) {
        textField.setText(newValue.replaceAll("[^[0-9]|.]", ""));
      }
    });
  }

  private static void maxCharsLimiter(TextField textField, final int maxLength) {
    textField.textProperty().addListener((ov, oldValue, newValue) -> {
      if (textField.getText().length() > maxLength) {
        String s = textField.getText().substring(0, maxLength);
        textField.setText(s);
      }
    });
  }

  @FXML
  public void initialize() {
    fields = Map.ofEntries(
        entry(Field.NAME,
            new ItemField(nameInput, false, itemField -> item.setName(itemField.getStringValue()),
                item::getName, nameErrorLabel)),

        entry(Field.BRAND,
            new ItemField(brandInput, true, itemField -> item.setBrand(itemField.getStringValue()),
                item::getBrand, sharedErrorLabel)),

        entry(Field.AMOUNT,
            new ItemField(amountInput, false, itemField -> item.setAmount(itemField.getIntegerValue()),
                item::getAmount, sharedErrorLabel)),

        entry(Field.REGULAR_PRICE,
            new ItemField(ordinaryPriceInput, true, itemField -> item.setRegularPrice(itemField.getDoubleValue()),
                item::getRegularPrice, sharedErrorLabel)),

        entry(Field.SALE_PRICE,
            new ItemField(salesPriceInput, true, itemField -> item.setSalePrice(itemField.getDoubleValue()),
                item::getSalePrice, sharedErrorLabel)),

        entry(Field.PURCHASE_PRICE,
            new ItemField(retailerPriceInput, true, itemField -> item.setPurchasePrice(itemField.getDoubleValue()),
                item::getPurchasePrice, sharedErrorLabel)),

        entry(Field.SECTION,
            new ItemField(placementSectionInput, true, itemField -> item.setSection(itemField.getStringValue()),
                item::getSection, sharedErrorLabel)),

        entry(Field.ROW,
            new ItemField(placementRowInput, true, itemField -> item.setRow(itemField.getStringValue()),
                item::getRow, sharedErrorLabel)),

        entry(Field.SHELF,
            new ItemField(placementShelfInput, true, itemField -> item.setShelf(itemField.getStringValue()),
                item::getShelf, sharedErrorLabel)),

        entry(Field.HEIGHT,
            new ItemField(dimensionsHeightInput, true, itemField -> item.setHeight(itemField.getDoubleValue()),
                item::getHeight, sharedErrorLabel)),

        entry(Field.WIDTH,
            new ItemField(dimensionsWidthInput, true, itemField -> item.setWidth(itemField.getDoubleValue()),
                item::getWidth, sharedErrorLabel)),

        entry(Field.LENGTH,
            new ItemField(dimensionsLengthInput, true, itemField -> item.setLength(itemField.getDoubleValue()),
                item::getLength, sharedErrorLabel)),

        entry(Field.WEIGHT,
            new ItemField(weightInput, true, itemField -> item.setWeight(itemField.getDoubleValue()),
                item::getWeight, sharedErrorLabel)),

        entry(Field.BARCODE, 
            new ItemField(barcodeInput, true, itemField -> item.setBarcode(itemField.getStringValue()),
                item::getBarcode, sharedErrorLabel))
    );
  }

  protected boolean isEditing() {
    return editing;
  }

  protected void close() {
    warehouseController.removeDetailsViewController(item);
    stage.close();
  }

  protected void showDetailsView() {
    stage.show();
    stage.setIconified(false);
    stage.requestFocus();
    this.update();
    addInputValidationListeners();
    nameInput.setOnMouseClicked(e -> nameInput.selectAll());
  }

  private void addInputValidationListeners() {
    fields.get(Field.NAME).addValidators(new NotEmptyValidator());
    fields.get(Field.BRAND).addValidators();
    fields.get(Field.AMOUNT).addValidators(new IntegerValidator(), new NotNegativeValidator(), new MaxValueValidator(CoreConst.MAX_AMOUNT));

    fields.get(Field.REGULAR_PRICE).addValidators(new DoubleValidator(), new NotNegativeValidator());
    fields.get(Field.SALE_PRICE).addValidators(new DoubleValidator(), new NotNegativeValidator());
    fields.get(Field.PURCHASE_PRICE).addValidators(new DoubleValidator(), new NotNegativeValidator());

    fields.get(Field.SECTION).addValidators(new MaxLengthValidator(placementMaxLength));
    fields.get(Field.ROW).addValidators(new MaxLengthValidator(placementMaxLength));
    fields.get(Field.SHELF).addValidators(new MaxLengthValidator(placementMaxLength));

    fields.get(Field.HEIGHT).addValidators(new DoubleValidator(), new NotNegativeValidator());
    fields.get(Field.LENGTH).addValidators(new DoubleValidator(), new NotNegativeValidator());
    fields.get(Field.WIDTH).addValidators(new DoubleValidator(), new NotNegativeValidator());

    fields.get(Field.WEIGHT).addValidators(new DoubleValidator(), new NotNegativeValidator());
    fields.get(Field.BARCODE).addValidators(new BarcodeValidator());
  }

  private void update() {
    for (ItemField field : fields.values()) {
      field.updateField();
    }
    
    if (item.getBarcode() != null) {
      generateBarcodeImage();
    }
  }

  private void generateBarcodeImage() {
    try (InputStream imageInputStream = BarcodeCreator
        .generateBarcodeImageInputStream(item.getBarcode().substring(0, 12))) {
      Image barcodeImage = new Image(imageInputStream);
      barcodeImageView.setImage(barcodeImage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void saveItem() {
    nameErrorLabel.setText("");
    barcodeErrorLabel.setText("");
    if (fields.get(Field.NAME).getStringValue() == null) {
      nameErrorLabel.setText("Legg til produktnavn for å lagre.");
      return;
    }
    String barcode = barcodeInput.getText();
    BarcodeValidator barcodeValidator = new BarcodeValidator();
    if (barcode.length() > 0 && barcode.length() < 13) {
      barcodeErrorLabel.setText("Barcode må inneholde 13 tall.");
      return;
    } else if (barcode.length() != 0 && !barcodeValidator.validateInput(barcode)) {
      barcodeErrorLabel.setText("Kontrolltallet stemmet ikke med de 12 første tallene.");
      return;
    }
    for (ItemField field : fields.values()) {
      if (!field.isValid()) {
        return;
      }
    }
    item.doBatchChanges(() -> {
      for (ItemField field : fields.values()) {
        field.saveField();
      }
    });
    if (!warehouse.containsItem(item.getId())) {
      barcodeErrorLabel.setText("");
      warehouse.addItem(item);
    }
    toggleEditing();
    update();
  }
  
  @FXML private void promptRemoveItem() {
    if (item.getName().equals("")) {
      stage.close();
      return;
    }

    Alert promptDeleteConfirmationAlert = new Alert(AlertType.WARNING);
    promptDeleteConfirmationAlert.setHeaderText("Er du sikker på at du vil slette " + item.getName() + "?");
    promptDeleteConfirmationAlert.setContentText("Denne handlingen kan ikke angres");
    promptDeleteConfirmationAlert.initStyle(StageStyle.UTILITY);
    ButtonType dontDeleteButtonType = new ButtonType("Ikke slett", ButtonData.CANCEL_CLOSE);
    ButtonType confirmDeleteButtonType = new ButtonType("Slett", ButtonData.OK_DONE);

    promptDeleteConfirmationAlert.getButtonTypes().setAll(dontDeleteButtonType, confirmDeleteButtonType);

    promptDeleteConfirmationAlert.showAndWait()
    .filter(response -> response == confirmDeleteButtonType)
        .ifPresent(response -> removeItem());
  }

  private void removeItem() {
    warehouse.removeItem(item.getId());
    warehouseController.removeDetailsViewController(item);
    stage.close();
  }

  @FXML
  private void decrement() {
    amountInput.setText(String.valueOf(Integer.parseInt(amountInput.getText()) - 1));
  }

  @FXML
  private void increment() {
    amountInput.setText(String.valueOf(Integer.parseInt(amountInput.getText()) + 1));
  }

  @FXML
  protected void toggleEditing() {
    editing = !editing;
    for (ItemField field : fields.values()) {
      field.setDisabled(!editing);
    }
    
    incrementButton.setDisable(!editing);
    decrementButton.setDisable(!editing);

    WarehouseController.setRegionVisibility((Region) saveButton.getParent(), editing);
    WarehouseController.setRegionVisibility((Region) editButton.getParent(), !editing);

    editButton.setVisible(!editing);
    saveButton.setVisible(editing);
    saveButton.setDisable(!editing);
    deleteButton.setVisible(editing);
    deleteButton.setDisable(!editing);

    deleteButton.prefWidthProperty().bind(sectionSaveDelete.widthProperty().divide(2));
    saveButton.prefWidthProperty().bind(sectionSaveDelete.widthProperty().divide(2));
  }

  public String toString() {
    return "" + nameInput.getText() + "\n" + brandInput.getText() + "\n" + amountInput.getText() + "\n"
        + ordinaryPriceInput.getText() + "\n" + salesPriceInput.getText() + "\n" + retailerPriceInput.getText() + "\n"
        + placementSectionInput.getText() + "\n" + placementRowInput.getText() + "\n" + placementShelfInput.getText() + "\n"
        + dimensionsLengthInput.getText() + "\n" + dimensionsWidthInput.getText() + "\n" + dimensionsHeightInput.getText()
        + "\n" + weightInput.getText() + "\n" + barcodeInput.getText();
  }
  
  protected ScrollPane getScrollPane() {
    return detailsViewScrollPane;
  }
}
