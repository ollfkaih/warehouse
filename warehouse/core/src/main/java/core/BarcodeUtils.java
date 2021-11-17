package core;

/**
 * Validates that the input is a valid EAN-13 barcode.
 */
public abstract class BarcodeUtils {
  public static boolean validateBarcode(String barcode) {
    if (barcode.length() == 13) {
      int total = 0;
      for (int i = 0; i < 12; i++) {
        total += Integer.parseInt("" + barcode.charAt(i)) * (i % 2 == 0 ? 1 : 3);
      }
      int checkDigit = 10 - (total % 10);
      if (String.valueOf(checkDigit).equals("" + barcode.charAt(12))) {
        return true;
      }
    }
    return false;
  }
}
