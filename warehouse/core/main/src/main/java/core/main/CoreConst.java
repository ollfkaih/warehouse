package core.main;

/**
 * Collection of constants that are used in the core module, and that might be changed in the future.
 */
public abstract class CoreConst {
  public static final int MIN_AMOUNT = 0;
  public static final int MAX_AMOUNT = Integer.MAX_VALUE - 1;

  public static final double MIN_PRICE = 0;
  public static final double MAX_PRICE = Double.MAX_VALUE - 1;

  public static final double MIN_WEIGHT = 0;
  public static final double MAX_WEIGHT = Double.MAX_VALUE - 1;

  public static final int MAX_POSITION_LENGTH = 3;

  public static final double MIN_ITEM_DIMENSION = 0;
  public static final double MAX_ITEM_DIMENSION = Double.MAX_VALUE - 1;

  public static final int MAX_BARCODE_LENGTH = 13;
  
  /**
   * Options for different values to sort items by.
   */
  public enum SortOption {
    NAME, AMOUNT, PRICE, WEIGHT, DATE, STATUS
  }
}
