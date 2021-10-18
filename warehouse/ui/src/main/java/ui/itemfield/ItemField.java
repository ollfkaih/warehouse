package ui.itemfield;

import javafx.scene.control.TextField;
import ui.validators.InputValidator;
import ui.validators.NotEmptyValidatior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for a textField that corresponds to an item property.
 */
public class ItemField {
  /**
   * Interface for a function that saves an itemField textField value to the corresponding item property.
   */
  public static interface Saver {
    void saveItemField(ItemField itemField);
  }

  /**
   * Interface for a function that gets the item property value for the property the field corresponds to. 
   */
  public static interface Getter {
    Object getItemValue();
  }
  
  protected final TextField textField;
  protected final Saver saver;
  protected final Getter getter;
  protected final boolean nullable;
  protected List<InputValidator> validators = new ArrayList<>();

  public ItemField(TextField textField, boolean nullable, Saver saver, Getter getter) {
    this.textField = textField;
    this.nullable = nullable;
    this.saver = saver;
    this.getter = getter;

    addChangeValidator();
  }

  private void addChangeValidator() {
    InputValidator notEmptyValidator = new NotEmptyValidatior();
    textField.textProperty().addListener((obs, oldv, value) -> {
      boolean valid;

      boolean isEmpty = !notEmptyValidator.validateInput(value);
      if (!nullable && isEmpty) {
        valid = false;
      } else if (nullable && isEmpty) {
        valid = true;
      } else {
        valid = true;

        for (InputValidator validator : validators) {
          if (!validator.validateInput(value)) {
            valid = false;
            break;
          }
        }
      }
      
      setFieldValidity(valid);
    });
  }

  public void saveField() {
    try {
      saver.saveItemField(this);
      setFieldValidity(true);
    } catch (Exception e) {
      // TODO(eikhr): Give user feedback on what the problem is
      setFieldValidity(false);
    }
  }

  public void updateField() {
    Object itemProperty = getter.getItemValue();
    textField.setText(itemProperty == null ? "" : String.valueOf(itemProperty));
  }

  private void setFieldValidity(boolean legal) {
    textField.getStyleClass().removeAll(Arrays.asList("illegalInput"));
    if (!legal) {
      textField.getStyleClass().add("illegalInput");
    }
  }
  
  public void addValidators(InputValidator... validators) {
    for (InputValidator validator : validators) {
      this.validators.add(validator);
    }
  }

  public void setDisabled(boolean disabled) {
    textField.setDisable(disabled);
  }

  /**
   * Gets the textFields value as a String.
   */
  public String getStringValue() {
    if (textField.getText() == null || textField.getText().equals("")) {
      return null;
    } else {
      return textField.getText();
    }
  }

  /**
   * Gets the textFields value as a Double.
   */
  public Double getDoubleValue() {
    if (textField.getText() == null || textField.getText().equals("")) {
      return null;
    } else {
      return Double.valueOf(textField.getText());
    }
  }

  /**
   * Gets the textFields value as a Integer.
   */
  public Integer getIntegerValue() {
    if (textField.getText() == null || textField.getText().equals("")) {
      return null;
    } else {
      return Integer.valueOf(textField.getText());
    }
  }
}
