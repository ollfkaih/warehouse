package ui.validators;

/**
 * Validates that the input length is longer or equal to the minLength.
 */
public class MinLengthValidator implements InputValidator {
  private int minLength;

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
