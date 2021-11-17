package ui.validators;

/**
 * Validates that the input is a valid EAN-13 barcode.
 */
public class BarcodeValidator implements InputValidator {
  private static MinLengthValidator minLengthValidator = new MinLengthValidator(13);
  private static MaxValueValidator maxLengthValidator = new MaxValueValidator(13);

  @Override
  public boolean validateInput(String input) {
    if (minLengthValidator.validateInput(input) && maxLengthValidator.validateInput(input)) {
      int total = 0;
      for (int i = 0; i < 12; i++) {
        total += Integer.parseInt("" + input.charAt(i)) * (i % 2 == 0 ? 1 : 3);
      }
      int checkDigit = 10 - (total % 10);
      if (String.valueOf(checkDigit).equals("" + input.charAt(12))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getErrorMessage() {
    return "Barcode format is wrong";
  }
}
