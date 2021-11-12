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
  private final HBox textAmountAndButtonsHBox;
  private final VBox brandAndNameVBox;
  
  private final Button incrementButton;
  private final Button decrementButton;
  
  public ItemElementAnchorPane(Item item) {
    super.paddingProperty().set(new Insets(5));
    super.setMinHeight(50);
    Label nameLabel = new Label(item.getName());
    nameLabel.setFont(boldFont);
    
    if (item.getBrand() != null) {
      Label brandLabel = new Label(item.getBrand());
      brandAndNameVBox = new VBox(brandLabel, nameLabel);
      AnchorPane.setTopAnchor(brandAndNameVBox, 2d);
    } else {
      brandAndNameVBox = new VBox(nameLabel);
      AnchorPane.setTopAnchor(brandAndNameVBox, 10d);
    }
    nameLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
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
    AnchorPane.setTopAnchor(textAmountAndButtonsHBox, 7d);
    HBox.setHgrow(textAmountAndButtonsHBox, Priority.ALWAYS);
    HBox.setHgrow(brandAndNameVBox, Priority.NEVER);
  }

  protected void setButtonsVisible(boolean visible) {
    incrementButton.setVisible(visible);
    decrementButton.setVisible(visible);
  }
  
  protected Button getDecrementButton() {
    return decrementButton;
  }
  
  protected Button getIncrementButton() {
    return incrementButton;
  }
}
