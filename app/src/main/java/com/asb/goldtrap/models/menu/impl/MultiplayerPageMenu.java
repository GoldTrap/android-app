package com.asb.goldtrap.models.menu.impl;

import com.asb.goldtrap.models.menu.CardGridMenu;
import com.asb.goldtrap.models.menu.types.MultiPlayerPageMenuType;

/**
 * Created by arjun on 14/01/16.
 */
public class MultiPlayerPageMenu implements CardGridMenu {
    private String name;
    private String image;
    private MultiPlayerPageMenuType type;
    private int spanSize;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultiPlayerPageMenuType getType() {
        return type;
    }

    public void setType(MultiPlayerPageMenuType type) {
        this.type = type;
    }

    @Override
    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }
}
