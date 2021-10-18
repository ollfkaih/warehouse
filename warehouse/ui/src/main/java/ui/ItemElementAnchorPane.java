package ui;

import core.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
  private VBox brandAndNameVBox;
  
  private final Button incrementButton;
  private final Button decrementButton;
  
  public ItemElementAnchorPane(Item item) {
    super.paddingProperty().set(new Insets(5));
    
    Label brandText = new Label(item.getBrand());
    Label nameText = new Label(item.getName());
    nameText.setFont(boldFont);
    brandAndNameVBox = new VBox(brandText, nameText);
    nameText.setTextOverrun(OverrunStyle.ELLIPSIS);
    AnchorPane.setLeftAnchor(brandAndNameVBox, 20d);
    AnchorPane.setRightAnchor(brandAndNameVBox, 200d);
    
    incrementButton = new Button("+");
    incrementButton.setId("incrementButton");
    incrementButton.setMinWidth(25);
    incrementButton.setMinHeight(25);
    incrementButton.setVisible(false);
    decrementButton = new Button("-");
    decrementButton.setId("decrementButton");
    decrementButton.setMinWidth(25);
    decrementButton.setMinHeight(25);
    decrementButton.setVisible(false);

    Label amountLabel = new Label(String.valueOf(item.getAmount()));
    amountLabel.setMinWidth(80);
    amountLabel.setFont(boldFont);
    amountLabel.setAlignment(Pos.CENTER);
    textAmountAndButtonsHBox = new HBox(decrementButton, amountLabel, incrementButton);
    textAmountAndButtonsHBox.setSpacing(10);
    textAmountAndButtonsHBox.setAlignment(Pos.CENTER);
    AnchorPane.setRightAnchor(textAmountAndButtonsHBox, 20d);
    
    super.getChildren().addAll(brandAndNameVBox, textAmountAndButtonsHBox);
    AnchorPane.setTopAnchor(brandAndNameVBox, 5d);
    AnchorPane.setTopAnchor(textAmountAndButtonsHBox, 5d);
    HBox.setHgrow(textAmountAndButtonsHBox, Priority.ALWAYS);
    HBox.setHgrow(brandAndNameVBox, Priority.NEVER);
  }

  protected void setButtonsVisible(boolean visible) {
    incrementButton.setVisible(visible);
    decrementButton.setVisible(visible);
  }
  
  public Button getDecrementButton() {
    return decrementButton;
  }
  
  public Button getIncrementButton() {
    return incrementButton;
  }
}
