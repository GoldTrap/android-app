package com.asb.goldtrap.views.drawers.impl.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;

/**
 * Created by arjun on 17/09/15.
 */
public class HorizontalLineDrawer extends AbstractLineDrawer {

    public HorizontalLineDrawer(Paint aiPaint, Paint playerPaint) {
        super(aiPaint, playerPaint);
    }

    @Override
    protected boolean shouldDrawLine(Paint paint, DotsGameSnapshot brain, int row, int col) {
        return null != paint;
    }

    @Override
    protected void drawLine(Canvas canvas, float lineWidth,
                            float lineHeight, float x, float y, Paint paint) {
        canvas.drawRect(x, y - lineHeight * 0.05f, x + lineWidth, y + lineHeight * 0.05f,
                paint);
    }

    @Override
    protected LineState[][] getLines(DotsGameSnapshot brain) {
        return brain.getHorizontalLines();
    }
}
