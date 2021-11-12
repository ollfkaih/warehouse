package ui;

import static java.util.Map.entry;

import core.ClientWarehouse;
import core.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.itemfield.ItemField;
import ui.validators.BarcodeValidator;
import ui.validators.DoubleValidator;
import ui.validators.IntegerValidator;
import ui.validators.MaxLengthValidator;
import ui.validators.NotNegativeValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This controller shows a separate window with all the properties of an item,
 * and the possibilty to change propeties of the selected item.
 */
public class DetailsViewController {
  @FXML private ScrollPane detailsViewScrollPane;

  @FXML private TextField inpName;
  @FXML private TextField inpBrand;
  @FXML private TextField inpAmount;
  @FXML private Button btnIncrement;
  @FXML private Button btnDecrement;
  @FXML private TextField inpPlacementSection;
  @FXML private TextField inpPlacementRow;
  @FXML private TextField inpPlacementShelf;
  @FXML private TextField inpOrdinaryPrice;
  @FXML private TextField inpSalesPrice;
  @FXML private TextField inpRetailerPrice;
  @FXML private ComboBox<String> comboBoxSalesTax;
  @FXML private TextField inpDimensionsLength;
  @FXML private TextField inpDimensionsWidth;
  @FXML private TextField inpDimensionsHeigth;
  @FXML private TextField inpWeight;
  @FXML private TextField inpBarcode;
  @FXML private Button btnEdit;
  @FXML private Button btnSave;
  @FXML private Button btnDelete;
  @FXML private ImageView barcodeImageView;

  private final Stage stage;
  private Parent detailsRoot;

  private final Item item;
  private final ClientWarehouse warehouse;
  private final WarehouseController warehouseController;

  private static final int SAFEBOUND_TOP = 30;
  private static final int SAFEBOUND_BOTTOM = 75;

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
      stage.getIcons().add(new Image(WarehouseApp.class.getResourceAsStream("icon/1-rounded.png")));
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (!warehouse.getCurrentUser().isAdmin()) {
      btnEdit.setVisible(false);
    }
  }

  @FXML
  public void initialize() {
    fields = Map.ofEntries(
        entry(Field.NAME,
            new ItemField(inpName, false, itemField -> item.setName(itemField.getStringValue()),
                item::getName)),

        entry(Field.BRAND,
            new ItemField(inpBrand, true, itemField -> item.setBrand(itemField.getStringValue()),
                item::getBrand)),

        entry(Field.AMOUNT,
            new ItemField(inpAmount, false, itemField -> item.setAmount(itemField.getIntegerValue()),
                item::getAmount)),

        entry(Field.REGULAR_PRICE,
            new ItemField(inpOrdinaryPrice, true, itemField -> item.setRegularPrice(itemField.getDoubleValue()),
                item::getRegularPrice)),

        entry(Field.SALE_PRICE,
            new ItemField(inpSalesPrice, true, itemField -> item.setSalePrice(itemField.getDoubleValue()),
                item::getSalePrice)),

        entry(Field.PURCHASE_PRICE,
            new ItemField(inpRetailerPrice, true, itemField -> item.setPurchasePrice(itemField.getDoubleValue()),
                item::getPurchasePrice)),

        entry(Field.SECTION,
            new ItemField(inpPlacementSection, true, itemField -> item.setSection(itemField.getStringValue()),
                item::getSection)),

        entry(Field.ROW,
            new ItemField(inpPlacementRow, true, itemField -> item.setRow(itemField.getStringValue()),
                item::getRow)),

        entry(Field.SHELF,
            new ItemField(inpPlacementShelf, true, itemField -> item.setShelf(itemField.getStringValue()),
                item::getShelf)),

        entry(Field.HEIGHT,
            new ItemField(inpDimensionsHeigth, true, itemField -> item.setHeight(itemField.getDoubleValue()),
                item::getHeight)),

        entry(Field.WIDTH,
            new ItemField(inpDimensionsWidth, true, itemField -> item.setWidth(itemField.getDoubleValue()),
                item::getWidth)),

        entry(Field.LENGTH,
            new ItemField(inpDimensionsLength, true, itemField -> item.setLength(itemField.getDoubleValue()),
                item::getLength)),

        entry(Field.WEIGHT,
            new ItemField(inpWeight, true, itemField -> item.setWeight(itemField.getDoubleValue()),
                item::getWeight)),

        entry(Field.BARCODE, 
            new ItemField(inpBarcode, true, itemField -> item.setBarcode(itemField.getStringValue()),
                item::getBarcode))
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
  }

  private void addInputValidationListeners() {
    fields.get(Field.NAME).addValidators();
    fields.get(Field.BRAND).addValidators();
    fields.get(Field.AMOUNT).addValidators(new IntegerValidator(), new NotNegativeValidator());

    fields.get(Field.REGULAR_PRICE).addValidators(new DoubleValidator(), new NotNegativeValidator());
    fields.get(Field.SALE_PRICE).addValidators(new DoubleValidator(), new NotNegativeValidator());
    fields.get(Field.PURCHASE_PRICE).addValidators(new DoubleValidator(), new NotNegativeValidator());

    fields.get(Field.SECTION).addValidators(new MaxLengthValidator(2));
    fields.get(Field.ROW).addValidators(new MaxLengthValidator(2));
    fields.get(Field.SHELF).addValidators(new MaxLengthValidator(2));

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
    for (ItemField field : fields.values()) {
      field.saveField();
    }
    if (!warehouse.containsItem(item.getId())) {
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
    inpAmount.setText(String.valueOf(Integer.parseInt(inpAmount.getText()) - 1));
  }

  @FXML
  private void increment() {
    inpAmount.setText(String.valueOf(Integer.parseInt(inpAmount.getText()) + 1));
  }

  @FXML
  protected void toggleEditing() {
    editing = !editing;
    for (ItemField field : fields.values()) {
      field.setDisabled(!editing);
    }

    setRegionVisibility(btnDecrement, editing);
    setRegionVisibility(btnIncrement, editing);

    setRegionVisibility((Region) btnSave.getParent(), editing);

    btnEdit.setVisible(!editing);

    btnSave.setVisible(editing);
    btnSave.setDisable(!editing);
    btnDelete.setVisible(editing);
    btnDelete.setDisable(!editing);
  }

  private void setRegionVisibility(Region region, boolean visible) {
    region.setDisable(!visible);
    region.setVisible(visible);
    region.setMinWidth(visible ? -1 : 0);
    region.setMinHeight(visible ? -1 : 0);
    region.setMaxWidth(visible ? -1 : 0);
    region.setMaxHeight(visible ? -1 : 0);
  }

  public String toString() {
    return "" + inpName.getText() + "\n" + inpBrand.getText() + "\n" + inpAmount.getText() + "\n"
        + inpOrdinaryPrice.getText() + "\n" + inpSalesPrice.getText() + "\n" + inpRetailerPrice.getText() + "\n"
        + inpPlacementSection.getText() + "\n" + inpPlacementRow.getText() + "\n" + inpPlacementShelf.getText() + "\n"
        + inpDimensionsLength.getText() + "\n" + inpDimensionsWidth.getText() + "\n" + inpDimensionsHeigth.getText()
        + "\n" + inpWeight.getText() + "\n" + inpBarcode.getText();
  }

  
  protected ScrollPane getScrollPane() {
    return detailsViewScrollPane;
  }
}
