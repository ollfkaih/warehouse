package ui;

import core.Item;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ItemElementHBox extends HBox {
  final Font boldFont = new Font("Arial Bold", 13);
  private HBox textAmountAndButtonsHBox;
  private VBox nameAndStatusVBox;
  
  private final Button incrementButton;
  private final Button decrementButton;
  private final Button removeButton;
  
  public ItemElementHBox(Item item) {
    super.setSpacing(25);
    super.paddingProperty().set(new Insets(10));
    
    Label nameText = new Label(item.getName());
    nameText.setFont(boldFont);
    Label statusText = new Label("Status: ");
    nameAndStatusVBox = new VBox(nameText, statusText);
    nameText.setTextOverrun(OverrunStyle.ELLIPSIS);
    
    Label textAmountLabel = new Label("Antall");
    textAmountLabel.setFont(boldFont);
    Label amountLabel = new Label(String.valueOf(item.getAmount()));
    VBox textAmount = new VBox(textAmountLabel, amountLabel);
    incrementButton = new Button("+");
    decrementButton = new Button("-");
    textAmountAndButtonsHBox = new HBox(decrementButton, textAmount, incrementButton);
    textAmountAndButtonsHBox.setSpacing(25);
    textAmountAndButtonsHBox.setPrefWidth(150);
    textAmountAndButtonsHBox.setMinWidth(150);
    
    removeButton = new Button("Slett");
    removeButton.setStyle("-fx-background-color: #D95C5C;");
    removeButton.setMinWidth(60);
    
    super.getChildren().addAll(nameAndStatusVBox, textAmountAndButtonsHBox, removeButton);
    HBox.setHgrow(nameAndStatusVBox, Priority.ALWAYS);
  }
  
  public Button getDecrementButton() {
    return decrementButton;
  }
  
  public Button getRemoveButton() {
    return removeButton;
  }
  
  public Button getIncrementButton() {
    return incrementButton;
  }
}
