package com.asb.goldtrap.models.eo;

import com.asb.goldtrap.models.buyables.BuyableType;

/**
 * Buyable.
 * Created by arjun on 17/04/16.
 */
public class Buyable {
    private BuyableType type;
    private String image;
    private String name;
    private String description;

    public BuyableType getType() {
        return type;
    }

    public void setType(BuyableType type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
