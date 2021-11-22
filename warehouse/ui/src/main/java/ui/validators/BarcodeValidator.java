package ui.validators;

import core.BarcodeUtils;

/**
 * Validates that the input is a valid EAN-13 barcode.
 */
public class BarcodeValidator implements InputValidator {
  @Override
  public boolean validateInput(String input) {
    return BarcodeUtils.validateBarcode(input);
  }

  @Override
  public String getErrorMessage() {
    return "Barcode format is wrong";
  }
}
