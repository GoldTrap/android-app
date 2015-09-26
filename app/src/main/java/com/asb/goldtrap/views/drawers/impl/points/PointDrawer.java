package com.asb.goldtrap.views.drawers.impl.points;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

/**
 * Created by arjun on 17/09/15.
 */
public class PointDrawer implements BoardComponentDrawer {
    private Paint paint;

    public PointDrawer(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain) {
        int cols = brain.getHorizontalLines()[0].length + 1;
        int rows = brain.getHorizontalLines().length;
        float lineWidth = width / (cols);
        float lineHeight = height / (rows);

        float pointRadius = Math.min(lineHeight, lineWidth) * 0.1f;
        float x = lineWidth / 2;
        float y = lineHeight / 2;
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                canvas.drawCircle(x, y, pointRadius, paint);
                x += lineWidth;
            }
            x = lineWidth / 2;
            y += lineHeight;
        }
    }
}
