package ui.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BarcodeValidatorTest {
  private BarcodeValidator barcodeValidator = new BarcodeValidator();

  @Test
  @DisplayName("Barcode validator returns true for a valid barcode")
  void testValidBarcode() {
    assertTrue(barcodeValidator.validateInput("5439873205437"));
    assertTrue(barcodeValidator.validateInput("7450002347239"));
  }

  @Test
  @DisplayName("Barcode validator returns false for a invalid barcodes")
  void testInvalidBarcodes() {
    assertFalse(barcodeValidator.validateInput("5439873205435"));
    assertFalse(barcodeValidator.validateInput("1234567890123"));
  }

  @Test
  @DisplayName("Barcode validator returns false for wrong length barcodes")
  void testWrongLengthBarcodes() {
    assertFalse(barcodeValidator.validateInput("12345434"));
    assertFalse(barcodeValidator.validateInput("12345678901231"));
  }
}
