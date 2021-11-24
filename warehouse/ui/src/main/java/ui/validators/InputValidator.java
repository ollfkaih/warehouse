package ui.validators;

/**
 * Interface for validators of input fields. validateInput takes the input string and returns true if valid, and false if not.
 */
public interface InputValidator {
  /**
   * Method to validate an input with custom validation rules.
   *
   * @param input string to validate
   * @return boolean indicating if input is valid
   */
  public boolean validateInput(String input);
  
  /**
   * Returns the reason to why an InputValidator returned false.
   *
   * @return String errorMessage
   */
  public String getErrorMessage();
}
