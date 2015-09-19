package com.asb.goldtrap.views.drawers.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

/**
 * Created by arjun on 17/09/15.
 */
public class HorizontalLineDrawer extends AbstractLineDrawer {

    public HorizontalLineDrawer(Paint aiPaint, Paint playerPaint) {
        super(aiPaint, playerPaint);
    }

    @Override
    protected void drawLine(Canvas canvas, DotsGameSnapshot brain, float lineWidth, float lineHeight,
                            int row, int col, float x,
                            float y, Paint paint) {
        if (brain.getLastClickedLineType() == LineType.HORIZONTAL
                && row == brain.getLastClickedRow()
                && col == brain.getLastClickedCol()) {
            // don't draw the last clicked line
        }
        else {
            canvas.drawLine(x, y, x + lineWidth, y, paint);
        }
    }

    @Override
    protected LineState[][] getLines(DotsGameSnapshot brain) {
        return brain.getHorizontalLines();
    }
}
