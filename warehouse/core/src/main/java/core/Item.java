package core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private int id;
    private String name;
    private int amount;

    public Item(
            @JsonProperty("id") int id, 
            @JsonProperty("name") String name, 
            @JsonProperty("amount") int amount
        ) {
        setName(name);
        setId(id);
        setAmount(amount);    
}

    public Item(int id, String name) {
        this(id, name, 0);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        } 
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
        if (getAmount() > CoreConst.MINAMOUNT + 1) {
            setAmount(getAmount() - 1);
        }
    }
}
