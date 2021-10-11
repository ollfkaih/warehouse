package ui;

import core.Item;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * This element is an item in the list over items.
 */
public class ItemElementAnchorPane extends AnchorPane {
  final Font boldFont = new Font("Arial Bold", 13);
  private HBox textAmountAndButtonsHBox;
  private VBox nameAndStatusVBox;
  
  private final Button incrementButton;
  private final Button decrementButton;
  
  public ItemElementAnchorPane(Item item) {
    super.paddingProperty().set(new Insets(5));
    
    Label nameText = new Label(item.getName());
    nameText.setFont(boldFont);
    Label statusText = new Label("Status: ");
    nameAndStatusVBox = new VBox(nameText, statusText);
    nameText.setTextOverrun(OverrunStyle.ELLIPSIS);
    AnchorPane.setLeftAnchor(nameAndStatusVBox, 20d);
    
    Label textAmountLabel = new Label("Antall");
    textAmountLabel.setFont(boldFont);
    Label amountLabel = new Label(String.valueOf(item.getAmount()));
    VBox textAmount = new VBox(textAmountLabel, amountLabel);
    incrementButton = new Button("+");
    decrementButton = new Button("-");
    HBox.setHgrow(textAmount, Priority.ALWAYS);
    textAmountAndButtonsHBox = new HBox(decrementButton, textAmount, incrementButton);
    textAmountAndButtonsHBox.setSpacing(10);
    textAmountAndButtonsHBox.setMinWidth(150);
    AnchorPane.setRightAnchor(textAmountAndButtonsHBox, 20d);
    
    super.getChildren().addAll(nameAndStatusVBox, textAmountAndButtonsHBox);
    AnchorPane.setTopAnchor(nameAndStatusVBox, 5d);
    AnchorPane.setTopAnchor(textAmountAndButtonsHBox, 5d);
    HBox.setHgrow(textAmountAndButtonsHBox, Priority.ALWAYS);
  }
  
  public Button getDecrementButton() {
    return decrementButton;
  }
  
  public Button getIncrementButton() {
    return incrementButton;
  }
}
