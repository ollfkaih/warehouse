package ui.itemfield;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ui.WarehouseController;
import ui.validators.InputValidator;
import ui.validators.NotEmptyValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wrapper for a textField that corresponds to an item property.
 */
public class ItemField {
  /**
   * Interface for a function that saves an itemField textField value to the
   * corresponding item property.
   */
  @FunctionalInterface
  public interface SaveFunction {
    void saveItemField(ItemField itemField);
  }

  /**
   * Interface for a function that gets the item property value for the property
   * the field corresponds to.
   */
  @FunctionalInterface
  public interface GetFunction {
    Object getItemValue();
  }

  protected final TextField textField;
  protected final SaveFunction saveFunction;
  protected final GetFunction getFunction;
  protected final boolean nullable;
  protected final Label errorLabel;
  protected String errorMessage;
  protected List<InputValidator> validators = new ArrayList<>();

  public ItemField(TextField textField, boolean nullable, SaveFunction saveFunction, GetFunction getFunction, Label errorLabel) {
    this.textField = textField;
    this.nullable = nullable;
    this.saveFunction = saveFunction;
    this.getFunction = getFunction;
    this.errorLabel = errorLabel;

    addChangeValidator();
  }

  private void addChangeValidator() {
    textField.textProperty().addListener((observableValue, oldValue, newValue) -> this.handleChange(newValue));
  }

  private void handleChange(String value) {
    InputValidator notEmptyValidator = new NotEmptyValidator();
    boolean isEmpty = !notEmptyValidator.validateInput(value);
    if (!nullable && isEmpty) {
      setError("Feltet kan ikke være tomt");
      return;
    } else if (nullable && isEmpty) {
      setError(null);
      return;
    }

    for (InputValidator validator : validators) {
      if (!validator.validateInput(value)) {
        setError(validator.getErrorMessage());
        return;
      }
    }

    setError(null);
  }

  public void saveField() {
    try {
      saveFunction.saveItemField(this);
      setError(null);
    } catch (Exception e) {
      setError(e.getMessage());
    }
  }

  public void updateField() {
    Object itemProperty = getFunction.getItemValue();
    textField.setText(itemProperty == null ? "" : String.valueOf(itemProperty));
  }

  public boolean isValid() {
    return this.errorMessage == null;
  }

  public String getErrorMessage() {
    return this.errorMessage;
  }

  private void setError(String error) {
    boolean legal = error == null;
    errorMessage = error;
    textField.getStyleClass().removeAll(Collections.singletonList("illegalInput"));
    if (!legal) {
      textField.getStyleClass().add("illegalInput");
      errorLabel.setText(error);
      WarehouseController.setRegionVisibility(errorLabel, true);
    } else {
      WarehouseController.setRegionVisibility(errorLabel, false);
    }
  }

  public void addValidators(InputValidator... validators) {
    Collections.addAll(this.validators, validators);
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
