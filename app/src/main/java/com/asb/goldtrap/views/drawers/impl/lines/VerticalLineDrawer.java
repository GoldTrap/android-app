package com.asb.goldtrap.views.drawers.impl.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;

/**
 * Created by arjun on 17/09/15.
 */
public class VerticalLineDrawer extends AbstractLineDrawer {

    public VerticalLineDrawer(Paint aiPaint, Paint playerPaint) {
        super(aiPaint, playerPaint);
    }

    @Override
    protected boolean shouldDrawLine(Paint paint, DotsGameSnapshot brain, int row, int col) {
        return null != paint;
    }

    @Override
    protected void drawLine(Canvas canvas, float lineWidth, float lineHeight, float x, float y,
                            Paint paint) {
        canvas.drawRect(x - lineHeight * 0.05f, y, x + lineHeight * 0.05f, y + lineHeight,
                paint);
    }

    @Override
    protected LineState[][] getLines(DotsGameSnapshot brain) {
        return brain.getVerticalLines();
    }
}
