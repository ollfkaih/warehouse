package ui.validators;

/**
 * Validates that the input is longer or equal to the minLength.
 */
public class MinLengthValidator implements InputValidator {
  int minLength;

  public MinLengthValidator(int minLength) {
    this.minLength = minLength;
  }

  @Override
  public boolean validateInput(String input) {
    return input.length() >= minLength;
  }

  @Override
  public String getErrorMessage() {
    return "Length is too short";
  }
}
