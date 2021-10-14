package ui.validators;

/**
 * Validates that the input is not a negative number.
 */
public class NotNegativeValidator implements InputValidator {
  static final InputValidator validator = new MinValueValidator(0);

  @Override
  public boolean validateInput(String input) {
    return validator.validateInput(input);
  }
}
