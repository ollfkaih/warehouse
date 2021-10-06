package ui;

import core.CoreConst.SortOptions;
import core.Item;
import core.Warehouse;
import data.DataPersistence;
import data.WarehouseFileSaver;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;

public class WarehouseController {
  private static final String FILENAME = "warehouse";

  private Warehouse warehouse;
  private final DataPersistence dataPersistence = new WarehouseFileSaver(FILENAME);

  @FXML private AnchorPane root;
  @FXML private GridPane dividerGridPane;
  @FXML private TextField newProductName;
  @FXML private ScrollPane itemContainer;
  @FXML private VBox itemList;
  @FXML private TextField searchInput;
  @FXML private ComboBox<Enum<SortOptions>> sortBySelector;
  @FXML private Button orderByButton;
  @FXML private VBox sortAndOrderSelectors;
  @FXML private VBox titleAddandSearch;

  private SortOptions sortBy = SortOptions.Date;
  private boolean ascending = true;

  private Map<Item, DetailsViewController> detailsViewControllers = new HashMap<>();

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

    updateInventory();
    enterPressed();
        
    // adds values to combobox
    for (SortOptions values : SortOptions.values()) {
      sortBySelector.getItems().add(values);
    }

    searchInput.textProperty().addListener((observable, oldValue, newValue) -> updateInventory());
  }

  @FXML
  private void updateInventory() {
    itemList.getChildren().clear();
    List<Item> items = getItems();
    for (int i = 0; i < items.size(); i++) {
      ItemElementHBox itemElement = new ItemElementHBox(items.get(i));

      String id = items.get(i).getId();
      itemElement.getDecrementButton().setOnAction(e -> decrementAmount(id));
      itemElement.getIncrementButton().setOnAction(e -> incrementAmount(id));
      itemElement.getRemoveButton().setOnAction(e -> removeItem(id));
      
      if (warehouse.findItem(id).getAmount() == 0) {
        itemElement.getDecrementButton().setDisable(true);
      }

      itemList.getChildren().add(itemElement);
      detailsViewControllers.putIfAbsent(items.get(i), new DetailsViewController(items.get(i)));
      
      int index = i;

      itemElement.setOnMouseClicked(e -> detailsViewControllers.get(items.get(index)).showDetailsView());
    }
  }

  private List<Item> getItems() {
    List<Item> items = warehouse.getAllItemsSorted(sortBy, ascending);
    return items
        .stream()
        .filter(item -> item.getName().contains(searchInput.getText()))
        .collect(Collectors.toList());
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
    updateInventory();
    saveWarehouse();

    newProductName.requestFocus();
    newProductName.selectAll();
  }

  @FXML
  private void removeItem(String id) {
    warehouse.removeItem(warehouse.findItem(id));
    updateInventory();
    saveWarehouse();
  }

  protected void incrementAmount(String id) {
    warehouse.findItem(id).incrementAmount();
    updateInventory();
    saveWarehouse();
  }

  protected void decrementAmount(String id) {
    warehouse.findItem(id).decrementAmount();
    updateInventory();
    saveWarehouse();
  }

  @FXML
  private void changeSortBy() {

    String value = "";
    if (sortBySelector.getValue() != null) {
      value = sortBySelector.getValue().toString();
    }

    switch (value) {
      case "Date":
        sortBy = SortOptions.Date;
        break;
      case "Amount":
        sortBy = SortOptions.Amount;
        break;
      case "Name":
        sortBy = SortOptions.Name;
        break;
      case "Status":
        sortBy = SortOptions.Status;
        break;
      default:
        sortBy = SortOptions.Name;
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

  private void saveWarehouse() {
    try {
      dataPersistence.saveWarehouse(warehouse);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
} 
