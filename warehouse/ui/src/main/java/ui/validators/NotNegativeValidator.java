package ui.validators;

/**
 * Validates that the input is not a negative number.
 */
public class NotNegativeValidator implements InputValidator {
  private static final InputValidator validator = new MinValueValidator(0);

  @Override
  public boolean validateInput(String input) {
    return validator.validateInput(input);
  }

  @Override
  public String getErrorMessage() {
    return "Number cannot be negative";
  }
}
