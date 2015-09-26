package com.asb.goldtrap.views.drawers.impl.cells;

import android.graphics.Paint;

import java.util.Set;

/**
 * Created by arjun on 26/09/15.
 */
public class CellDrawerThatSkipsLastScoredCells extends CellDrawer {

    public CellDrawerThatSkipsLastScoredCells(Paint aiPaint, Paint playerPaint) {
        super(aiPaint, playerPaint);
    }

    protected boolean shouldPaintTheCell(Paint paint, Set<String> lookup, int row, int col) {
        return super.shouldPaintTheCell(paint, lookup, row, col) &&
                !lookup.contains(row + ", " + col);
    }

}
