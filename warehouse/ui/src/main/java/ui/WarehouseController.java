package ui;

import core.Warehouse;

import java.util.ArrayList;

import core.Item;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
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
    Pane scrollPane;

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
        scrollPane.getChildren().clear();
        ArrayList<Pane> itemPaneList = new ArrayList<>();
        for (int i=0; i<warehouse.getAllItems().size(); i++) {
            int id = warehouse.getAllItems().get(i).getId();
            itemPaneList.add(new Pane());
            itemPaneList.get(i).setPrefHeight(70);
            itemPaneList.get(i).setPrefWidth(460);
            itemPaneList.get(i).setStyle("-fx-background-color: #f9f9f9;");
            
            //Text
            Text[] textList = new Text[5];
            for (Text text: textList){
                text= new Text(10,10,"Hei");
                text.setStrokeType(StrokeType.OUTSIDE);
                text.setStrokeWidth(0);
            }

            textList[0].setLayoutX(20);
            textList[0].setLayoutY(28);
            textList[0].setText(warehouse.getAllItems().get(i).getName());
            textList[0].setFont(new Font("Arial Bond",13));
            
            textList[1].setLayoutX(20);
            textList[1].setLayoutY(44);
            textList[1].setText("SKU/ID: " + warehouse.getAllItems().get(i).getId());

            textList[2].setLayoutX(20);
            textList[2].setLayoutY(61);
            textList[2].setText("Status: ");

            textList[3].setLayoutX(222);
            textList[3].setLayoutY(36);
            textList[3].setText("Antall");

            textList[4].setLayoutX(222);
            textList[4].setLayoutY(50);
            textList[4].setText(String.valueOf(warehouse.getAllItems().get(i).getAmount()));
            textList[0].setFont(new Font("Arial Bond",13));
            
            for (Text text: textList){
                itemPaneList.get(i).getChildren().add(text);
            }

            //Buttons
            Button[] buttonList = new Button[3];
            for (Button button: buttonList){
                button= new Button("Button");
                button.setLayoutY(23);
                button.setMnemonicParsing(false);
                button.setTextFill(Color.WHITE);
            }
 
            buttonList[0].setLayoutX(195);
            buttonList[0].setOnAction((e) -> 
                {decrementAmount(id);
                });
            buttonList[0].setStyle("-fx-background-color: #5CA0D9;");
            buttonList[0].setText("-");

            buttonList[1].setLayoutX(300);
            buttonList[1].setOnAction((e) -> 
                {incrementAmount(id);
                });
            buttonList[1].setStyle("-fx-background-color: #5CA0D9;");
            buttonList[1].setText("+");

            buttonList[0].setLayoutX(390);
            buttonList[0].setOnAction((e) -> 
                {removeItem(id);
                });
            buttonList[0].setStyle("-fx-background-color: #D95C5C;");
            buttonList[0].setText("Slett");
            
            for (Button button: buttonList){
                itemPaneList.get(i).getChildren().add(button);
            }

            scrollPane.getChildren().add(itemPaneList.get(i));
        }
    }

    @FXML
    private void addItem() {
        Item item = new Item(0, newProductName.getText());
        warehouse.addItem(item);
        updateInventory();
    }

    @FXML
    private void removeItem(int id) {
        warehouse.removeItem(warehouse.findItem(id));
        updateInventory();
    }

    @FXML
    private void incrementAmount(int id) {
        //TODO
    }

    @FXML
    private void decrementAmount(int index) {
        //TODO
    }
    /*
    private String createNewItemTextInstance(String name, String status, int amount, int id) {
    	return ""
    			+ "                        <Pane prefHeight=\"70.0\" prefWidth=\"459.0\" style=\"-fx-background-color: #f9f9f9;\">\n"
    			+ "                           <children>\n"
    			+ "                              <Text layoutX=\"19.0\" layoutY=\"28.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"" + name + "\">\n"
    			+ "                                 <font>\n"
    			+ "                                    <Font name=\"Arial Bold\" size=\"13.0\" />\n"
    			+ "                                 </font>\n"
    			+ "                              </Text>\n"
    			+ "                              <Text layoutX=\"19.0\" layoutY=\"44.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"SKU/ID: " + id + "\" />\n"
    			+ "                              <Text layoutX=\"20.0\" layoutY=\"61.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"Status: " + status + "\" wrappingWidth=\"124\" />\n"
    			+ "                              <Text layoutX=\"222.0\" layoutY=\"50.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"Antall\" textAlignment=\"CENTER\" wrappingWidth=\"75.0\" />\n"
    			+ "                              <Text layoutX=\"222.0\" layoutY=\"36.0\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"500000\" textAlignment=\"CENTER\" wrappingWidth=\"75.0\">\n"
    			+ "                                 <font>\n"
    			+ "                                    <Font name=\"Arial Black\" size=\"13.0\" />\n"
    			+ "                                 </font>\n"
    			+ "                              </Text>\n"
    			+ "                              <Button layoutX=\"194.0\" layoutY=\"24.0\" mnemonicParsing=\"false\" onAction=\"#decrementItem\" prefHeight=\"20.0\" prefWidth=\"24.0\" style=\"-fx-background-color: #5CA0D9;\" text=\"-\" textFill=\"WHITE\" />\n"
    			+ "                              <Button layoutX=\"301.0\" layoutY=\"23.0\" mnemonicParsing=\"false\" onAction=\"#incrementItem\" style=\"-fx-background-color: #5CA0D9;\" text=\"+\" textFill=\"WHITE\" />\n"
    			+ "                              <Button layoutX=\"390.0\" layoutY=\"23.0\" mnemonicParsing=\"false\" onAction=\"#deleteItem\" style=\"-fx-background-color: #D95C5C;\" text=\"Slett\" textFill=\"WHITE\" />\n"
    			+ "                           </children>\n"
    			+ "                        </Pane>\n"
    			+ "";
    }
    */
}


