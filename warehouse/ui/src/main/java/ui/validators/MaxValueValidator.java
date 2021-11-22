package ui.validators;

/**
 * Validates that the input is less than or equal to the specified maxValue.
 */
public class MaxValueValidator implements InputValidator {
  int maxValue;

  public MaxValueValidator(int maxValue) {
    this.maxValue = maxValue;
  }

  @Override
  public boolean validateInput(String input) {
    return Double.parseDouble(input) <= maxValue;
  }

  @Override
  public String getErrorMessage() {
    return "Value cannot be greater than " + maxValue;
  }
}
