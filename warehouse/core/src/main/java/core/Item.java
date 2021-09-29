package core;

import java.util.UUID;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private String name;
    private int amount;
    private String id;
    private LocalDateTime creationDate;

    public Item(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("amount") int amount,
        @JsonProperty("creationDate") LocalDateTime creationDate
    ) {
        setId(id);
        setAmount(amount);
        setName(name);
        setCreationDate(creationDate);
    }

    public Item(String name, int amount) {
        this(UUID.randomUUID().toString(), name, amount, LocalDateTime.now());
    }

    public Item(String name) {
        this(name, 0);
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

    public LocalDateTime getCreationDate() {
        return creationDate;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
