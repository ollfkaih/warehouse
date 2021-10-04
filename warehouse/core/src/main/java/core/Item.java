package core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class Item {
  private String id;
  private String barcode;
  private String name;
  private String brand;
  private int amount;
  private double price;
  private double weight;
  private LocalDateTime creationDate;

  public Item(
      @JsonProperty("id") String id,
      @JsonProperty("barcode") String barcode,
      @JsonProperty("name") String name,
      @JsonProperty("brand") String brand,
      @JsonProperty("amount") int amount,
      @JsonProperty("price") double price,
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
    this(UUID.randomUUID().toString(),"barcode", name, "brand", amount, 0, 0, LocalDateTime.now());
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
    if (barcode == null) {
      throw new IllegalArgumentException();
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
    if (brand == null) {
      throw new IllegalArgumentException();
    }
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    if (price<CoreConst.MINAMOUNT || price>CoreConst.MAXAMOUNT) {
      throw new IllegalArgumentException("Price cannot be negative or larger than infinity");
    }
    this.price=price;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    if (price<CoreConst.MINAMOUNT || price>CoreConst.MAXAMOUNT) {
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

}
