package ui.validators;

/**
 * Validates that the input value is less than or equal to the specified maxValue.
 */
public class MaxValueValidator implements InputValidator {
  private final int maxValue;

  public MaxValueValidator(int maxValue) {
    this.maxValue = maxValue;
  }

  @Override
  public boolean validateInput(String input) {
    return Double.parseDouble(input) <= maxValue;
  }

  @Override
  public String getErrorMessage() {
    return "Value cannot be greater than " + Integer.toString(maxValue);
  }
}
