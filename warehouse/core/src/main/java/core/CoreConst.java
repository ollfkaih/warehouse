package core;

public abstract class CoreConst {
  public static final int MIN_AMOUNT = 0;
  public static final int MAX_AMOUNT = Integer.MAX_VALUE;

  public static final double MIN_PRICE = 0;
  public static final double MAX_PRICE = Double.POSITIVE_INFINITY;

  public static final double MIN_WEIGHT = 0;
  public static final double MAX_WEIGHT = Double.POSITIVE_INFINITY;

  public static final int MAX_SECTION_LENGTH = 2;
  public static final int MAX_RACK_LENGTH = 2;
  public static final int MAX_SHELF_LENGTH = 2;

  public static final double MIN_ITEM_DIMENSION = 0;
  public static final double MAX_ITEM_DIMENSION = Double.POSITIVE_INFINITY;

  public enum SortOptions {
    Name, Amount, Price, Weight, Date, Status
  }
}
