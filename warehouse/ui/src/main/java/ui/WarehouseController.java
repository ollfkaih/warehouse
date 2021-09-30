package ui;

import core.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.CoreConst.SortOptions;
import core.Item;
import data.IDataPersistence;
import data.WarehouseFileSaver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WarehouseController {
    private static final String FILENAME = "warehouse";

    private Warehouse warehouse;
    private final IDataPersistence dataPersistence = new WarehouseFileSaver(FILENAME);

    private SortOptions sortBy = SortOptions.Date;
    private boolean ascending = true;

    @FXML Pane textPane;
    @FXML TextField newProductName;
    @FXML VBox itemContainer;

    @FXML ComboBox<Enum<SortOptions>> sortBySelector;
    @FXML ComboBox<String> orderBySelector;

    @FXML TextField searchInput;

    @FXML
    void initialize() {
        try {
            warehouse = dataPersistence.getWarehouse();
        } catch (Exception e) {
            System.out.println("Could not load saved warehouse");
            System.out.println(e.toString());
        }
        if (warehouse == null) {
            warehouse=new Warehouse();
        }

        // adds values to combobox
        for (SortOptions values : SortOptions.values()) {
            sortBySelector.getItems().add(values);
        }
        orderBySelector.getItems().add("ASC");
        orderBySelector.getItems().add("DESC");
        
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> updateInventory());

        updateInventory();
        enterPressed();
        changeSortBy();
    }

    private List<Item> getItems() {
        List<Item> items = warehouse.getAllItemsSorted(sortBy, ascending); //TODO: When adding sorting options, call getAllItemsSorted with params
        return items
            .stream()
            .filter(item -> item.getName().contains(searchInput.getText()))
            .collect(Collectors.toList());
    }

    @FXML
    private void updateInventory() {
        itemContainer.getChildren().clear();
        ArrayList<Pane> itemPaneList = new ArrayList<>();

        List<Item> items = getItems();
        for (int i = 0; i < items.size(); i++) {
            String id = items.get(i).getId();
            itemPaneList.add(new Pane());
            itemPaneList.get(i).setPrefHeight(70);
            itemPaneList.get(i).setPrefWidth(460);
            itemPaneList.get(i).setStyle("-fx-background-color: #f9f9f9;");

            //Text
            Text textName = new Text(items.get(i).getName());
            setTextProperties(textName, 20, 36, new Font("Arial Bold",13));

            Text textStatus = new Text("Status: ");
            setTextProperties(textStatus, 20, 50);

            Text textAmountText = new Text("Antall");
            setTextProperties(textAmountText, 222, 36, new Font("Arial Bold", 13));

            Text textAmount = new Text(String.valueOf(items.get(i).getAmount()));
            setTextProperties(textAmount, 222, 50);

            //Buttons
            Button buttonRemove = new Button("Slett");
            setButtonPosition(buttonRemove, 390, 23);
            buttonRemove.setOnAction(e -> removeItem(id));
            buttonRemove.setStyle("-fx-background-color: #D95C5C;");

            Button buttonIncrement = new Button("+");
            setButtonPosition(buttonIncrement, 300, 23);
            buttonIncrement.setOnAction(e -> incrementAmount(id));

            Button buttonDecrement = new Button("-");
            buttonDecrement.setLayoutX(195);
            buttonDecrement.setLayoutY(23);
            buttonDecrement.setOnAction(e -> decrementAmount(id));
            
            if (warehouse.findItem(id).getAmount() == 0) {
                buttonDecrement.setDisable(true);
            }

            itemPaneList.get(i).getChildren().addAll(textName, textStatus, textAmountText, textAmount, buttonRemove, buttonIncrement, buttonDecrement);

            itemContainer.getChildren().add(itemPaneList.get(i));
        }
    }

    private void setButtonPosition(Button button, int xPos, int yPos) {
        button.setLayoutX(xPos);
        button.setLayoutY(yPos);
    }

    private void setTextProperties(Text textField, int xPos, int yPos, Font font) {
        setTextProperties(textField, xPos, yPos);
        textField.setFont(font);
    }

    private void setTextProperties(Text textField, int xPos, int yPos) {
        textField.setLayoutX(xPos);
        textField.setLayoutY(yPos);
    }

    private void enterPressed() {
        newProductName.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
              addItem();
            }
          } );
    }

    @FXML
    private void addItem() {
        Item item = new Item(newProductName.getText(), 0);
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

    @FXML
    private void incrementAmount(String id) {
        warehouse.findItem(id).incrementAmount();
        updateInventory();
        saveWarehouse();
    }

    @FXML
    private void decrementAmount(String id) {
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

        switch(value) {
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
        String orderByValue = "";
        if (orderBySelector.getValue() != null) {
            orderByValue = orderBySelector.getValue().toString();
        }
        switch(orderByValue) {
            case "ASC":
                ascending = true;
                break;
            case "DESC":
                ascending = false;
                break;
            default:
                ascending = true;
                break;
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
