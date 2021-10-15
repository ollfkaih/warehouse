package core;

/**
 * Collection of constants that are used in the core module, and that might be changed in the future.
 */
public abstract class CoreConst {
  public static final int MIN_AMOUNT = 0;
  public static final int MAX_AMOUNT = Integer.MAX_VALUE - 1;

  public static final double MIN_PRICE = 0;
  public static final double MAX_PRICE = Double.POSITIVE_INFINITY;

  public static final double MIN_WEIGHT = 0;
  public static final double MAX_WEIGHT = Double.POSITIVE_INFINITY;

  public static final int MAX_SECTION_LENGTH = 2;
  public static final int MAX_ROW_LENGTH = 2;
  public static final int MAX_SHELF_LENGTH = 2;

  public static final double MIN_ITEM_DIMENSION = 0;
  public static final double MAX_ITEM_DIMENSION = Double.POSITIVE_INFINITY;

  /**
   * Options for different values to sort items by.
   */
  public enum SortOptions {
    Name, Amount, Price, Weight, Date, Status
  }
}
