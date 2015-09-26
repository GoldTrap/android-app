package com.asb.goldtrap.views.drawers.impl.lines;

import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.views.LineType;

/**
 * Created by arjun on 26/09/15.
 */
public class VerticalLineDrawerThatSkipsLastSelectedLine extends VerticalLineDrawer {
    public VerticalLineDrawerThatSkipsLastSelectedLine(Paint aiPaint,
                                                       Paint playerPaint) {
        super(aiPaint, playerPaint);
    }

    @Override
    protected boolean shouldDrawLine(Paint paint, DotsGameSnapshot brain, int row, int col) {
        return super.shouldDrawLine(paint, brain, row, col) &&
                (brain.getLastClickedLineType() != LineType.VERTICAL
                        || row != brain.getLastClickedRow()
                        || col != brain.getLastClickedCol());
    }

}
