package com.asb.goldtrap.views.drawers.impl.achievements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.views.drawers.AnimatedBoardComponentDrawer;

/**
 * Created by arjun on 26/09/15.
 */
public class AchievementsDrawer implements AnimatedBoardComponentDrawer {
    public static final float SCALING_FACTOR = 0.45f;
    private static final String TAG = AchievementsDrawer.class.getSimpleName();
    private Paint paint;
    private Paint bitmapPaint;
    private Bitmap spark;
    private Rect scaledRect;

    public AchievementsDrawer(Paint paint, Paint bitmapPaint, Bitmap spark) {
        this.paint = paint;
        this.bitmapPaint = bitmapPaint;
        this.spark = spark;
        this.scaledRect = new Rect(0, 0, 0, 0);
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain,
                       long elapsedTime, long animationDuration) {
        Score score = brain.getScore();
        if (!score.getHorizontalLines().isEmpty()) {
            int cols = brain.getHorizontalLines()[0].length + 1;
            int rows = brain.getHorizontalLines().length;
            float lineWidth = width / (cols);
            float lineHeight = height / (rows);
            float percentage = (float) elapsedTime / (float) animationDuration;
            if (percentage > 1.0f) {
                percentage = 1.0f;
            }
            float percentageForEachLine = 1.0f / score.getHorizontalLines().size();
            int maxLinesToDrawCompletely = (int) (percentage / percentageForEachLine);
            float percentageOfLastLine =
                    (percentage - (maxLinesToDrawCompletely * percentageForEachLine)) /
                            percentageForEachLine;
            int linesDrawn = 0;
            int scaled = (int) (Math.min(lineHeight, lineWidth) * SCALING_FACTOR);

            for (Line line : score.getHorizontalLines()) {
                if (linesDrawn == maxLinesToDrawCompletely) {
                    drawSpark(canvas, width, height, line, lineWidth, lineHeight,
                            percentageOfLastLine, scaled);
                    break;
                }
                else {
                    drawSpark(canvas, width, height, line, lineWidth, lineHeight, 1.0f, scaled);
                }
                linesDrawn += 1;
            }
        }
    }

    private void drawSpark(Canvas canvas, int width, int height, Line line,
                           float lineWidth, float lineHeight, float percentageOfLastLine,
                           int scaled) {
        switch (line.lineType) {
            case HORIZONTAL:
                float x = width * percentageOfLastLine;
                float y = lineHeight + (lineHeight * line.row);
                int bitmapY = (int) (y - (lineHeight / 2) + ((lineHeight - scaled) / 2));
                int bitmapX = (int) (x - scaled);
                canvas.drawLine(0, y, x, y, paint);
                scaledRect.set(bitmapX, bitmapY, bitmapX + scaled, bitmapY + scaled);
                canvas.drawBitmap(spark, null, scaledRect, bitmapPaint);
                break;
            case VERTICAL:
                x = lineWidth + (lineWidth * line.col);
                y = height * percentageOfLastLine;
                bitmapY = (int) (y - scaled);
                bitmapX = (int) (x - (lineWidth / 2) + ((lineWidth - scaled) / 2));
                canvas.drawLine(x, 0, x, y, paint);
                scaledRect.set(bitmapX, bitmapY, bitmapX + scaled, bitmapY + scaled);
                canvas.drawBitmap(spark, null, scaledRect, bitmapPaint);
                break;
            case NONE:
                break;
        }
    }
}
