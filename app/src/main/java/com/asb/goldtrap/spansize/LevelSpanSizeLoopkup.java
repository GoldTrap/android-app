package com.asb.goldtrap.spansize;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by arjun on 07/02/16.
 */
public class LevelSpanSizeLoopkup extends GridLayoutManager.SpanSizeLookup {
    @Override
    public int getSpanSize(int position) {
        return 3;
    }
}
