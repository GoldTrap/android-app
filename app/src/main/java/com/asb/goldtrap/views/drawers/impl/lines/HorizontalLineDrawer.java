package com.asb.goldtrap.views.drawers.impl.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;

/**
 * Created by arjun on 17/09/15.
 */
public class HorizontalLineDrawer extends AbstractLineDrawer {

    public HorizontalLineDrawer(Paint aiPaint, Paint playerPaint, Paint blockedPaint) {
        super(aiPaint, playerPaint, blockedPaint);
    }

    @Override
    protected boolean shouldDrawLine(Paint paint, DotsGameSnapshot brain, int row, int col) {
        return null != paint;
    }

    @Override
    protected void drawLine(Canvas canvas, float lineWidth,
                            float lineHeight, float x, float y, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(x, y - lineHeight * 0.05f, x + lineWidth, y + lineHeight * 0.05f,
                    0.10f, 0.10f, paint);
        }
        else {
            canvas.drawRect(x, y - lineHeight * 0.05f, x + lineWidth, y + lineHeight * 0.05f,
                    paint);
        }
    }

    @Override
    protected LineState[][] getLines(DotsGameSnapshot brain) {
        return brain.getHorizontalLines();
    }
}
