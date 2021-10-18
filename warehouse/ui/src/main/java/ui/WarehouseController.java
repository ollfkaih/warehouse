package ui;

import core.CoreConst.SortOption;
import core.Item;
import core.Warehouse;
import core.WarehouseListener;
import data.DataPersistence;
import data.WarehouseFileSaver;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Controller class. Controls the main Warehouse view.
 */
public class WarehouseController implements WarehouseListener {
  private static final String FILENAME = "warehouse";

  private Warehouse warehouse;
  private final DataPersistence dataPersistence = new WarehouseFileSaver(FILENAME);

  @FXML private Label usernameLabel;
  @FXML private Button loginButton;
  @FXML private AnchorPane root;
  @FXML private GridPane dividerGridPane;
  @FXML private TextField newProductName;
  @FXML private ScrollPane itemContainer;
  @FXML private VBox itemList;
  @FXML private TextField searchInput;
  @FXML private ComboBox<String> sortBySelector;
  @FXML private Button orderByButton;
  @FXML private VBox sortAndOrderSelectors;
  @FXML private VBox titleAddandSearch;

  private SortOption sortBy = SortOption.Date;
  private boolean ascending = true;

  private Map<Item, DetailsViewController> detailsViewControllers = new HashMap<>();
  private LoginController loginController;

  @FXML
  void initialize() {
    try {
      warehouse = dataPersistence.getWarehouse();
    } catch (Exception e) {
      System.out.println("Could not load saved warehouse");
      System.out.println(e.toString());
    }
    if (warehouse == null) {
      warehouse = new Warehouse();
    }

    loginController = new LoginController(this, warehouse);

    updateInventory();
    enterPressed();
        
    List<String> displaySortStrings = List.of("Antall", "Dato", "Navn", "Pris", "Vekt");
    sortBySelector.getItems().addAll(displaySortStrings);

    searchInput.textProperty().addListener((observable, oldValue, newValue) -> updateInventory());

    warehouse.addListener(this);
  }

  @FXML
  private void login() {
    if (warehouse.getCurrentUser() == null) {
      loginController.showLoginView();
    } else {
      warehouse.removeCurrentUser();
      usernameLabel.setText("");
      loginButton.setText("Logg inn");
    }
  }

  protected void updateUser() {
    usernameLabel.setText(warehouse.getCurrentUser().getUserName());
    loginButton.setText("Logg ut");
  }

  @FXML
  private void updateInventory() {
    itemList.getChildren().clear();
    List<Item> items = getItems();
    for (int i = 0; i < items.size(); i++) {
      ItemElementAnchorPane itemElement = new ItemElementAnchorPane(items.get(i));

      String id = items.get(i).getId();
      itemElement.getDecrementButton().setOnAction(e -> decrementAmount(id));
      itemElement.getIncrementButton().setOnAction(e -> incrementAmount(id));
      
      if (warehouse.findItem(id).getAmount() == 0) {
        itemElement.getDecrementButton().setDisable(true);
      }

      itemList.getChildren().add(itemElement);
      detailsViewControllers.putIfAbsent(items.get(i), new DetailsViewController(items.get(i), this.warehouse, this));
      
      int index = i;

      itemElement.setOnMouseClicked(e -> detailsViewControllers.get(items.get(index)).showDetailsView());
      itemElement.setOnMouseEntered(e -> hover(itemElement, index));
      itemElement.setOnMouseExited(e -> notHover(itemElement, index));
      notHover(itemElement, index);
    }
  }

  private void notHover(Node itemElement, int i) {
    itemElement.getStyleClass().removeAll(Arrays.asList("hoverOverDark", "hoverOverLight"));
    if (i % 2 == 0) {
      itemElement.getStyleClass().add("notHoverOverLight");
    } else { 
      itemElement.getStyleClass().add("notHoverOverDark");
    }
  }

  private void hover(Node itemElement, int i) {
    itemElement.setCursor(Cursor.HAND);
    itemElement.getStyleClass().removeAll(Arrays.asList("notHoverOverDark", "notHoverOverLight"));
    if (i % 2 == 0) {
      itemElement.getStyleClass().add("hoverOverLight");
    } else { 
      itemElement.getStyleClass().add("hoverOverDark");
    }
  }

  protected void removeDetailsViewController(Item item) {
    if (detailsViewControllers.containsKey(item)) {
      detailsViewControllers.remove(item);
    }
    updateInventory();
  }

  protected List<Item> getItems() {
    return warehouse.getItemsSortedAndFiltered(sortBy, ascending, searchInput.getText());
  }

  private void enterPressed() {
    newProductName.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        addItem();
      }
    });
  }

  @FXML
  private void addItem() {
    Item item = new Item(newProductName.getText());
    warehouse.addItem(item);
    saveWarehouse();

    newProductName.requestFocus();
    newProductName.selectAll();
  }

  @FXML
  protected void removeItem(String id) {
    warehouse.removeItem(warehouse.findItem(id));
    saveWarehouse();
  }

  protected void incrementAmount(String id) {
    warehouse.findItem(id).incrementAmount();
    saveWarehouse();
  }

  protected void decrementAmount(String id) {
    warehouse.findItem(id).decrementAmount();
    saveWarehouse();
  }

  @FXML
  private void changeSortBy() {

    String value = "";
    if (sortBySelector.getValue() != null) {
      value = sortBySelector.getValue().toString();
    }

    switch (value) {
      case "Dato":
        sortBy = SortOption.Date;
        ascending = true;
        break;
      case "Antall":
        sortBy = SortOption.Amount;
        ascending = false;
        break;
      case "Navn":
        sortBy = SortOption.Name;
        ascending = true;
        break;
      case "Pris":
        sortBy = SortOption.Price;
        ascending = true;
        break;
      case "Vekt":
        sortBy = SortOption.Weight;
        ascending = true;
        break;
      default:
        sortBy = SortOption.Name;
        break;
    }
    updateInventory();
  }

  @FXML
  private void changeOrderBy() {
    ascending = !ascending;

    if (ascending && orderByButton.getStyleClass().contains("descending")) {
      orderByButton.getStyleClass().remove("descending");
    }
    if (!ascending && !orderByButton.getStyleClass().contains("descending")) {
      orderByButton.getStyleClass().add("descending");
    }

    updateInventory();
  }

  protected void saveWarehouse() {
    try {
      dataPersistence.saveWarehouse(warehouse);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  @Override
  public void itemAddedToWarehouse(Item item) {
    updateInventory();
  }

  @Override
  public void warehouseItemsUpdated() {
    updateInventory();
  }

  @Override
  public void itemRemovedFromWarehouse(Item item) {
    updateInventory();
  }

  // for testing purposes only
  protected HashMap<Item, DetailsViewController> getDetailViewControllers() {
    return new HashMap<Item, DetailsViewController>(detailsViewControllers);
  }
} 
