package com.asb.goldtrap.spansize;

import android.support.v7.widget.GridLayoutManager;

import com.asb.goldtrap.models.menu.CardGridMenu;

import java.util.List;

/**
 * Created by arjun on 22/11/15.
 */
public class MenuSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private List<? extends CardGridMenu> cardGridMenus;

    public MenuSpanSizeLookup(List<? extends CardGridMenu> cardGridMenus) {
        this.cardGridMenus = cardGridMenus;
    }

    @Override
    public int getSpanSize(int position) {
        return cardGridMenus.get(position).getSpanSize();
    }
}
