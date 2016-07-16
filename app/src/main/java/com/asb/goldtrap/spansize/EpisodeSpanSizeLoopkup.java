package com.asb.goldtrap.spansize;

import android.support.v7.widget.GridLayoutManager;

/**
 * Episode Span Size Loopkup.
 * Created by arjun on 16/07/16.
 */
public class EpisodeSpanSizeLoopkup extends GridLayoutManager.SpanSizeLookup {
    @Override
    public int getSpanSize(int position) {
        return 3;
    }
}
