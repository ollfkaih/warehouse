package ui;

import core.Warehouse;

import java.util.ArrayList;

import core.Item;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WarehouseController {
    private Warehouse warehouse;

    @FXML
    Pane textPane;

    @FXML
    VBox vBox;
    
    @FXML
    TextField newProductName;

    @FXML
    void initialize() {
        warehouse=new Warehouse();
        //something here
        updateInventory();
    }

    @FXML
    private void updateInventory() {
        vBox.getChildren().clear();
        ArrayList<Pane> itemPaneList = new ArrayList<>();
        System.out.println("Length: " + warehouse.getAllItems().size());
        for (int i=0; i<warehouse.getAllItems().size(); i++) {
            System.out.println("NAME: " + warehouse.getAllItems().get(i).getName());
            System.out.println("ID: " + warehouse.getAllItems().get(i).getId());
            System.out.println("AMOUNT: " + warehouse.getAllItems().get(i).getAmount());

            String id = warehouse.getAllItems().get(i).getId();
            itemPaneList.add(new Pane());
            itemPaneList.get(i).setPrefHeight(70);
            itemPaneList.get(i).setPrefWidth(460);
            itemPaneList.get(i).setStyle("-fx-background-color: #f9f9f9;");

            //Text
            Text textName = new Text(warehouse.getAllItems().get(i).getName());
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

            Text textAmount = new Text(String.valueOf(warehouse.getAllItems().get(i).getAmount()));
            textAmount.setStrokeType(StrokeType.OUTSIDE);
            textAmount.setStrokeWidth(0);
            textAmount.setLayoutX(222);
            textAmount.setLayoutY(50);

            itemPaneList.get(i).getChildren().addAll(textName);
            itemPaneList.get(i).getChildren().addAll(textStatus);
            itemPaneList.get(i).getChildren().addAll(textAmountText);
            itemPaneList.get(i).getChildren().addAll(textAmount);

            //Buttons
            Button buttonRemove = new Button();
            buttonRemove.setLayoutX(390);
            buttonRemove.setLayoutY(23);
            buttonRemove.setMnemonicParsing(false);
            buttonRemove.setTextFill(Color.WHITE);
            buttonRemove.setOnAction((e) -> 
                {removeItem(id);
                });
            buttonRemove.setStyle("-fx-background-color: #D95C5C;");
            buttonRemove.setText("Slett");

            Button buttonIncrement = new Button();
            buttonIncrement.setLayoutX(300);
            buttonIncrement.setLayoutY(23);
            buttonIncrement.setMnemonicParsing(false);
            buttonIncrement.setTextFill(Color.WHITE);
            buttonIncrement.setOnAction((e) -> 
                {incrementAmount(id);
                });
            buttonIncrement.setStyle("-fx-background-color: #5CA0D9;");
            buttonIncrement.setText("+");

            Button buttonDecrement = new Button();
            buttonDecrement.setLayoutX(195);
            buttonDecrement.setLayoutY(23);
            buttonDecrement.setMnemonicParsing(false);
            buttonDecrement.setTextFill(Color.WHITE);
            buttonDecrement.setOnAction((e) -> 
                {decrementAmount(id);
                });
            buttonDecrement.setStyle("-fx-background-color: #5CA0D9;");
            buttonDecrement.setText("-");

            
            Button[] buttonList = new Button[3];
            for (Button button: buttonList){
                button= new Button("Button");
                button.setLayoutY(23);
                button.setMnemonicParsing(false);
                button.setTextFill(Color.WHITE);
            }

            itemPaneList.get(i).getChildren().add(buttonRemove);
            itemPaneList.get(i).getChildren().add(buttonIncrement);
            itemPaneList.get(i).getChildren().add(buttonDecrement);

            vBox.getChildren().add(itemPaneList.get(i));
        }
    }

    @FXML
    private void addItem() {
        Item item = new Item(newProductName.getText(), 0);
        warehouse.addItem(item);
        updateInventory();
    }

    @FXML
    private void removeItem(String id) {
        warehouse.removeItem(warehouse.findItem(id));
        updateInventory();
    }

    @FXML
    private void incrementAmount(String id) {
        warehouse.findItem(id).incrementAmount();
        updateInventory();
    }

    @FXML
    private void decrementAmount(String id) {
        warehouse.findItem(id).decrementAmount();
        updateInventory();
    }
}


