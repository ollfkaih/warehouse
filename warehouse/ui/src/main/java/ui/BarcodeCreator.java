package ui;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Wrapper class for a static function that generates an InputStream with a barcode image from a barcode string.
 */
public abstract class BarcodeCreator {
  public static InputStream generateBarcodeImageInputStream(String barcodeText) throws Exception {
    Barcode barcode = BarcodeFactory.createEAN13(barcodeText);

    Path barcodesFolder = Path.of(System.getProperty("user.home"), "warehouse", "barcodes");
    Files.createDirectories(barcodesFolder);

    File file = barcodesFolder.resolve(barcodeText + ".png").toFile();
    if (file.createNewFile()) {
      System.out.println("File created: " + file.getName());

      BarcodeImageHandler.savePNG(barcode, file);
    } else {
      System.out.println("File already exists.");
    }

    return new FileInputStream(file);
  }
}