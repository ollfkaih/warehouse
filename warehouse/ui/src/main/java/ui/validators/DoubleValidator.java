package ui.validators;

/**
 * Validates that input string can be parsed as a Double.
 */
public class DoubleValidator implements InputValidator {
  @Override
  public boolean validateInput(String input) {
    try {
      Double.valueOf(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
