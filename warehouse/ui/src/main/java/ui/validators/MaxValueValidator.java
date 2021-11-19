package ui.validators;

/**
 * Validates that the input is shorter or equal to the specified maxValue.
 */
public class MaxValueValidator implements InputValidator {
  int maxValue;

  public MaxValueValidator(int maxValue) {
    this.maxValue = maxValue;
  }

  @Override
  public boolean validateInput(String input) {
    return input.length() <= maxValue;
  }

  @Override
  public String getErrorMessage() {
    return "Max " + maxValue + " characters are allowed";
  }
}
