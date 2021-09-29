package ui;

import core.Warehouse;

import java.util.ArrayList;
import java.util.List;

import core.CoreConst.SortOptions;
import core.Item;
import data.IDataPersistence;
import data.WarehouseFileSaver;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WarehouseController {
    private static final String FILENAME = "warehouse";

    private Warehouse warehouse;
    private final IDataPersistence dataPersistence = new WarehouseFileSaver(FILENAME);

    @FXML
    Pane textPane;
    
        
    @FXML
    TextField newProductName;

    @FXML
    VBox vBox;

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
        updateInventory();
        enterPressed();
    }

    @FXML
    private void updateInventory() {
        vBox.getChildren().clear();
        ArrayList<Pane> itemPaneList = new ArrayList<>();
        List<Item> allItems = warehouse.getAllItemsSorted(SortOptions.Date, true); //TODO: When adding sorting options, call getAllItemsSorted with params
        for (int i = 0; i < allItems.size(); i++) {

            String id = allItems.get(i).getId();
            itemPaneList.add(new Pane());
            itemPaneList.get(i).setPrefHeight(70);
            itemPaneList.get(i).setPrefWidth(460);
            itemPaneList.get(i).setStyle("-fx-background-color: #f9f9f9;");

            //Text
            Text textName = new Text(allItems.get(i).getName());
            textName.setStrokeType(StrokeType.OUTSIDE);
            textName.setStrokeWidth(0);
            textName.setLayoutX(20);
            textName.setLayoutY(36);
            textName.setFont(new Font("Arial Bold",13));

            Text textStatus = new Text("Status: ");
            textStatus.setStrokeType(StrokeType.OUTSIDE);
            textStatus.setStrokeWidth(0);
            textStatus.setLayoutX(20);
            textStatus.setLayoutY(50);

            Text textAmountText = new Text("Antall");
            textAmountText.setStrokeType(StrokeType.OUTSIDE);
            textAmountText.setStrokeWidth(0);
            textAmountText.setLayoutX(222);
            textAmountText.setLayoutY(36);
            textAmountText.setFont(new Font("Arial Bold",13));

            Text textAmount = new Text(String.valueOf(allItems.get(i).getAmount()));
            textAmount.setStrokeType(StrokeType.OUTSIDE);
            textAmount.setStrokeWidth(0);
            textAmount.setLayoutX(222);
            textAmount.setLayoutY(50);

            //Buttons
            Button buttonRemove = new Button();
            buttonRemove.setLayoutX(390);
            buttonRemove.setLayoutY(23);
            buttonRemove.setMnemonicParsing(false);
            buttonRemove.setTextFill(Color.WHITE);
            buttonRemove.setOnAction(e -> removeItem(id));
            buttonRemove.setStyle("-fx-background-color: #D95C5C;");
            buttonRemove.setText("Slett");

            Button buttonIncrement = new Button();
            buttonIncrement.setLayoutX(300);
            buttonIncrement.setLayoutY(23);
            buttonIncrement.setMnemonicParsing(false);
            buttonIncrement.setTextFill(Color.WHITE);
            buttonIncrement.setOnAction(e -> incrementAmount(id));
            buttonIncrement.setStyle("-fx-background-color: #5CA0D9;");
            buttonIncrement.setText("+");

            Button buttonDecrement = new Button();
            buttonDecrement.setLayoutX(195);
            buttonDecrement.setLayoutY(23);
            buttonDecrement.setMnemonicParsing(false);
            buttonDecrement.setTextFill(Color.WHITE);
            buttonDecrement.setOnAction(e -> decrementAmount(id));
            buttonDecrement.setStyle("-fx-background-color: #5CA0D9;");
            buttonDecrement.setText("-");
            
            if (warehouse.findItem(id).getAmount()==0) {
                buttonDecrement.setDisable(true);
            }


            itemPaneList.get(i).getChildren().addAll(textName, textStatus, textAmountText, textAmount, buttonRemove, buttonIncrement, buttonDecrement);

            vBox.getChildren().add(itemPaneList.get(i));
        }
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

    private void saveWarehouse() {
        try {
            dataPersistence.saveWarehouse(warehouse);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}


