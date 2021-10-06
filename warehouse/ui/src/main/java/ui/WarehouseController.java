package ui;

import core.CoreConst.SortOptions;
import core.Item;
import core.Warehouse;
import data.DataPersistence;
import data.WarehouseFileSaver;

import java.io.IOException;
import java.util.List;
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

  @FXML AnchorPane root;
  @FXML GridPane dividerGridPane;
  @FXML TextField newProductName;
  @FXML ScrollPane itemContainer;
  @FXML VBox itemList;
  @FXML TextField searchInput;
  @FXML ComboBox<Enum<SortOptions>> sortBySelector;
  @FXML Button orderByButton;
  @FXML VBox sortAndOrderSelectors;
  @FXML VBox titleAddandSearch;
  @FXML DetailsViewController detailsViewController;

  private SortOptions sortBy = SortOptions.Date;
  private boolean ascending = true;

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

    //TODO add something
    //showDetailsView();
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
      int index = i;
      itemElement.setOnMouseClicked(e -> showDetailsView(items.get(index)));
    }
  }

  private void showDetailsView(Item item) {
    final int safeBoundTop = 30;
    final int safeBoundBottom = 75;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsView.fxml"));
      Parent detailsRoot = loader.load();
             
      Stage stage = new Stage();
      stage.setScene(new Scene(detailsRoot));
      stage.setTitle("Edit: " + item.getName());
      stage.show();
      stage.setMinWidth(450);
      stage.setHeight(Screen.getPrimary().getBounds().getHeight() - safeBoundBottom);
      stage.setMaxHeight(detailsRoot.prefHeight(0));
      stage.setY(safeBoundTop);
  } catch (IOException ex) {
      System.err.println(ex);
  }

    return;
  }

  private List<Item> getItems() {
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
        ascending = true;
        break;
      case "Amount":
        sortBy = SortOptions.Amount;
        ascending = false;
        break;
      case "Name":
        sortBy = SortOptions.Name;
        ascending = true;
        break;
      case "Status":
        sortBy = SortOptions.Status;
        ascending = true;
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
