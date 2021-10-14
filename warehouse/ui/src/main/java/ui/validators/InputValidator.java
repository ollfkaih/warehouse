package ui.validators;

/**
 * Interface for validators of input fields. validateInput takes the input string and returns true if valid, and false if not.
 */
public interface InputValidator {
  public boolean validateInput(String input);
}