package ui.validators;

/**
 * Validates that the input is not null or empty.
 */
public class NotEmptyValidatior implements InputValidator {

  @Override
  public boolean validateInput(String input) {
    return input != null && !input.equals("");
  }

}
