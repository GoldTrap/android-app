package com.asb.goldtrap.models.menu.impl;

import com.asb.goldtrap.models.menu.CardGridMenu;
import com.asb.goldtrap.models.menu.types.HomePageMenuType;

/**
 * Created by arjun on 22/11/15.
 */
public class HomePageMenu implements CardGridMenu {
    private String name;
    private String image;
    private HomePageMenuType type;
    private int spanSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HomePageMenuType getType() {
        return type;
    }

    public void setType(HomePageMenuType type) {
        this.type = type;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }
}
