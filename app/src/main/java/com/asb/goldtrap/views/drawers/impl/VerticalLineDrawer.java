package com.asb.goldtrap.views.drawers.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

/**
 * Created by arjun on 17/09/15.
 */
public class VerticalLineDrawer extends AbstractLineDrawer {

    public VerticalLineDrawer(Paint aiPaint, Paint playerPaint) {
        super(aiPaint, playerPaint);
    }

    @Override
    protected void drawLine(Canvas canvas, DotsGameSnapshot brain, float lineWidth,
                            float lineHeight,
                            int row, int col, float x,
                            float y, Paint paint) {
        if (brain.getLastClickedLineType() == LineType.VERTICAL
                && row == brain.getLastClickedRow()
                && col == brain.getLastClickedCol()) {
            // don't draw the last clicked line
        }
        else {
            canvas.drawRect(x - lineHeight * 0.05f, y, x + lineHeight * 0.05f, y + lineHeight,
                    paint);
        }
    }

    @Override
    protected LineState[][] getLines(DotsGameSnapshot brain) {
        return brain.getVerticalLines();
    }
}
