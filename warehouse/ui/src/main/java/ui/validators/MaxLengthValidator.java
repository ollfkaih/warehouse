package ui.validators;

/**
 * Validates that the input is shorter or equal to the maxLength.
 */
public class MaxLengthValidator implements InputValidator {
  int maxLength;

  public MaxLengthValidator(int maxLength) {
    this.maxLength = maxLength;
  }

  @Override
  public boolean validateInput(String input) {
    return input.length() <= maxLength;
  }

  @Override
  public String getErrorMessage() {
    return "Length is too long";
  }
}
