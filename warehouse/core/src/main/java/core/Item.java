package core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * Class for representing one type of item.
 */
public class Item {
  private String id;
  private String name;
  private int amount;
  private String barcode;
  private String brand;
  private Double regularPrice;
  private Double salePrice;
  private Double purchasePrice;
  private String section;
  private String row;
  private String shelf;
  private Double height;
  private Double width;
  private Double length;
  private Double weight;
  private LocalDateTime creationDate;

  Collection<ItemListener> listeners = new ArrayList<>();

  public Item(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("amount") int amount,
      @JsonProperty("barcode") String barcode, @JsonProperty("brand") String brand,
      @JsonProperty("regularPrice") Double regularPrice, @JsonProperty("salePrice") Double salePrice,
      @JsonProperty("purchasePrice") Double purchasePrice, @JsonProperty("section") String section,
      @JsonProperty("row") String row, @JsonProperty("shelf") String shelf, @JsonProperty("itemHeight") Double height,
      @JsonProperty("itemWidth") Double width, @JsonProperty("itemLength") Double length,
      @JsonProperty("weight") Double weight, @JsonProperty("creationDate") LocalDateTime creationDate) {
    setId(id);
    setName(name);
    setAmount(amount);
    setBarcode(barcode);
    setBrand(brand);
    setRegularPrice(regularPrice);
    setSalePrice(salePrice);
    setPurchasePrice(purchasePrice);
    setSection(section);
    setRow(row);
    setShelf(shelf);
    setHeight(height);
    setWidth(width);
    setLength(length);
    setWeight(weight);
    setCreationDate(creationDate);
  }

  public Item(String name, int amount) {
    this(UUID.randomUUID().toString(), name, amount, null, null, null, null, null, null, null, null, null, null, null,
        null, LocalDateTime.now());
  }

  public Item(String name) {
    this(name, 0);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    if (id == null) {
      throw new IllegalArgumentException();
    }
    this.id = id;
    notifyChange();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException();
    }
    this.name = name;
    notifyChange();
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    if (amount < CoreConst.MIN_AMOUNT || amount > CoreConst.MAX_AMOUNT) {
      throw new IllegalArgumentException();
    }
    this.amount = amount;
    notifyChange();
  }

  public void incrementAmount() {
    if (amount == CoreConst.MAX_AMOUNT) {
      throw new IllegalStateException("Cannot increment any more");
    }
    setAmount(getAmount() + 1);
  }

  public void decrementAmount() {
    if (amount == CoreConst.MIN_AMOUNT) {
      throw new IllegalStateException("Cannot decrement any more");
    }
    setAmount(getAmount() - 1);
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
    notifyChange();
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
    notifyChange();
  }

  public Double getRegularPrice() {
    return regularPrice;
  }

  public void setRegularPrice(Double regularPrice) {
    if (regularPrice != null && (regularPrice < CoreConst.MIN_PRICE || regularPrice > CoreConst.MAX_PRICE)) {
      throw new IllegalArgumentException("Price cannot be negative or larger than infinity");
    }
    this.regularPrice = regularPrice;
    notifyChange();
  }

  @JsonIgnore
  public Double getCurrentPrice() {
    return salePrice != null ? salePrice : regularPrice;
  }

  public Double getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(Double salePrice) {
    if (salePrice != null && (salePrice < CoreConst.MIN_PRICE || salePrice > CoreConst.MAX_PRICE)) {
      throw new IllegalArgumentException("Price cannot be negative or larger than infinity");
    }
    this.salePrice = salePrice;
    notifyChange();
  }

  public Double getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(Double purchasePrice) {
    if (purchasePrice != null && (purchasePrice < CoreConst.MIN_PRICE || purchasePrice > CoreConst.MAX_PRICE)) {
      throw new IllegalArgumentException("Price cannot be negative or larger than infinity");
    }
    this.purchasePrice = purchasePrice;
    notifyChange();
  }

  public String getSection() {
    return section;
  }

  public void setSection(String section) {
    if (section != null && (section.length() > CoreConst.MAX_SECTION_LENGTH)) {
      throw new IllegalArgumentException(
          "Section length is too long. Max is " + CoreConst.MAX_SECTION_LENGTH + " characters");
    }
    this.section = section;
    notifyChange();
  }

  public String getRow() {
    return row;
  }

  public void setRow(String row) {
    if (row != null && (row.length() > CoreConst.MAX_ROW_LENGTH)) {
      throw new IllegalArgumentException("Row length is too long. Max is " + CoreConst.MAX_ROW_LENGTH + " characters");
    }
    this.row = row;
    notifyChange();
  }

  public String getShelf() {
    return shelf;
  }

  public void setShelf(String shelf) {
    if (shelf != null && (shelf.length() > CoreConst.MAX_SHELF_LENGTH)) {
      throw new IllegalArgumentException(
          "Shelf length is too long. Max is " + CoreConst.MAX_SHELF_LENGTH + " characters");
    }
    this.shelf = shelf;
    notifyChange();
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    if (height != null && (height < CoreConst.MIN_ITEM_DIMENSION || height > CoreConst.MAX_ITEM_DIMENSION)) {
      throw new IllegalArgumentException("Height is outside of allowed values");
    }
    this.height = height;
    notifyChange();
  }

  public Double getWidth() {
    return width;
  }

  public void setWidth(Double width) {
    if (width != null && (width < CoreConst.MIN_ITEM_DIMENSION || width > CoreConst.MAX_ITEM_DIMENSION)) {
      throw new IllegalArgumentException("Width is outside of allowed values");
    }
    this.width = width;
    notifyChange();
  }

  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    if (length != null && (length < CoreConst.MIN_ITEM_DIMENSION || length > CoreConst.MAX_ITEM_DIMENSION)) {
      throw new IllegalArgumentException("Length is outside of allowed values");
    }
    this.length = length;
    notifyChange();
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    if (weight != null && (weight < CoreConst.MIN_WEIGHT || weight > CoreConst.MAX_WEIGHT)) {
      throw new IllegalArgumentException("Weight is outside of allowed values");
    }
    this.weight = weight;
    notifyChange();
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
    notifyChange();
  }

  @Override
  public String toString() {
    return String.format(
        "Name: %s%n" + "Id: %s%n" + "Amount: %d%n" + "Brand: %s%n" + "Regular price: %f%n" + "Sale price: %f%n"
            + "Purchase price: %f%n" + "Warehouse placement: %s %s %s%n" + "Item dimensions: %f*%f*%f%n"
            + "Weight: %f%n",
        getName(), getId(), getAmount(), getBrand(), getRegularPrice(), getSalePrice(), getPurchasePrice(),
        getSection(), getRow(), getShelf(), getWidth(), getLength(), getHeight(), getWeight());
  }

  private void notifyChange() {
    for (ItemListener listener : listeners) {
      listener.itemUpdated();
    }
  }

  public void addListener(ItemListener listener) {
    listeners.add(listener);
  }

  public void removeListener(ItemListener listener) {
    listeners.remove(listener);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, barcode, brand, creationDate, height, id, length, name, purchasePrice, regularPrice,
        row, salePrice, section, shelf, weight, width);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Item other = (Item) obj;
    return amount == other.amount && Objects.equals(barcode, other.barcode) && Objects.equals(brand, other.brand)
        && Objects.equals(creationDate, other.creationDate) && Objects.equals(height, other.height)
        && Objects.equals(id, other.id) && Objects.equals(length, other.length) && Objects.equals(name, other.name)
        && Objects.equals(purchasePrice, other.purchasePrice) && Objects.equals(regularPrice, other.regularPrice)
        && Objects.equals(row, other.row) && Objects.equals(salePrice, other.salePrice)
        && Objects.equals(section, other.section) && Objects.equals(shelf, other.shelf)
        && Objects.equals(weight, other.weight) && Objects.equals(width, other.width);
  }
}
