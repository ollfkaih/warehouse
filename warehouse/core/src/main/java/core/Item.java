package core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

public class Item { 
  private String id;
  private String barcode;
  private String name;
  private String brand;
  private int amount;
  private Double price;
  private double weight;
  private LocalDateTime creationDate;

  public Item(
      @JsonProperty("id") String id,
      @JsonProperty("barcode") String barcode,
      @JsonProperty("name") String name,
      @JsonProperty("brand") String brand,
      @JsonProperty("amount") int amount,
      @JsonProperty("price") Double price,
      @JsonProperty("weight") double weight,
      @JsonProperty("creationDate") LocalDateTime creationDate
  ) {
    setId(id);
    setBarcode(barcode);
    setName(name);
    setBrand(brand);
    setAmount(amount);
    setPrice(price);
    setCreationDate(creationDate);
  }

  public Item(String name, int amount) {
    this(UUID.randomUUID().toString(), null, name, null, amount, null, 0, LocalDateTime.now());
  }

  public Item(String name) {
    this(name, 0);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    if (barcode != null) {
      if (barcode.length() != 13) {
        throw new IllegalArgumentException("Barcode must have length 13");
      }
      if (!barcode.matches("^[0-9]+$")) {
        throw new IllegalArgumentException("Barcode can only contain numbers");
      }
    }
    this.barcode = barcode;
  }

  
  public String getName() {
    return name;
  }
  

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException();
    }
    this.name = name;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand=brand;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    if (amount < CoreConst.MINAMOUNT) {
      throw new IllegalArgumentException();
    }
    this.amount = amount;
  }

  public void incrementAmount() {
    if (getAmount() >= CoreConst.MAXAMOUNT) {
      return;
    }
    setAmount(getAmount() + 1);
  }

  public void decrementAmount() {
    if (getAmount() > CoreConst.MINAMOUNT) {
      setAmount(getAmount() - 1);
    }
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    if (price != null && (price<CoreConst.MINAMOUNT || price>CoreConst.MAXAMOUNT)) {
      throw new IllegalArgumentException("Price cannot be negative or larger than infinity");
    }
    this.price=price;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    if (weight<CoreConst.MINAMOUNT || weight>CoreConst.MAXAMOUNT) {
      throw new IllegalArgumentException("Weight cannot be negative or larger than infinity");
    }
    this.weight=weight;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  private void setCreationDate(LocalDateTime date) {
    if (date == null) {
      throw new IllegalArgumentException("Date must be set");
    }
    if (LocalDateTime.now().compareTo(date) < 0) {
      throw new IllegalArgumentException("Date cannot be in the future");
    }
    this.creationDate = date;
  }

  @Override
  public String toString() {
    return "Name: " + getName() + " Amout: " + getAmount() + " Price: " + price + " Date: " + getCreationDate() + "\n";
  }


  public static final Comparator<Item> nameComparator = (Item i1, Item i2) -> {
    return i1.getName().compareTo(i2.getName());
  };

  public static final Comparator<Item> amountComparator = (Item i1, Item i2) -> {
    return Integer.compare(i1.getAmount(), (i2.getAmount()));
  };

  public static final Comparator<Item> priceComparator = (Item i1, Item i2) -> {
    if (i1.getPrice() == null) {
      return 1;
    }
    if (i2.getPrice() == null) {
      return -1;
    }
    return Double.compare(i1.getPrice().doubleValue(), (i2.getPrice().doubleValue()));
  };

  public static final Comparator<Item> weightComparator = (Item i1, Item i2) -> {
    return Double.compare(i1.getWeight(), (i2.getWeight()));
  };

  public static final Comparator<Item> createdDateComparator = (Item i1, Item i2) -> {
    return i2.getCreationDate().compareTo(i1.getCreationDate());
  };
}
