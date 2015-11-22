package com.asb.goldtrap.spansize;

import android.support.v7.widget.GridLayoutManager;

import com.asb.goldtrap.models.menu.HomePageMenu;

import java.util.List;

/**
 * Created by arjun on 22/11/15.
 */
public class MenuSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private List<HomePageMenu> homePageMenus;

    public MenuSpanSizeLookup(List<HomePageMenu> homePageMenus) {
        this.homePageMenus = homePageMenus;
    }

    @Override
    public int getSpanSize(int position) {
        return homePageMenus.get(position).getSpanSize();
    }
}
