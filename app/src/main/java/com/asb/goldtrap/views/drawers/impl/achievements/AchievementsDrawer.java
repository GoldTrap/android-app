package com.asb.goldtrap.views.drawers.impl.achievements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.views.drawers.AnimatedBoardComponentDrawer;

/**
 * Created by arjun on 26/09/15.
 */
public class AchievementsDrawer implements AnimatedBoardComponentDrawer {
    public static final float SCALING_FACTOR = 0.50f;
    private static final String TAG = AchievementsDrawer.class.getSimpleName();
    private Paint paint;
    private Paint bitmapPaint;
    private Bitmap spark;
    private Bitmap scaledSpark;

    public AchievementsDrawer(Paint paint, Paint bitmapPaint, Bitmap spark) {
        this.paint = paint;
        this.bitmapPaint = bitmapPaint;
        this.spark = spark;
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain,
                       long elapsedTime, long animationDuration) {
        Score score = brain.getScore();
        if (!score.getLines().isEmpty()) {
            int cols = brain.getHorizontalLines()[0].length + 1;
            int rows = brain.getHorizontalLines().length;
            float lineWidth = width / (cols);
            float lineHeight = height / (rows);
            float percentage = (float) elapsedTime / (float) animationDuration;
            if (percentage > 1.0f) {
                percentage = 1.0f;
            }
            float percentageForEachLine = 1.0f / score.getLines().size();
            int maxLinesToDrawCompletely = (int) (percentage / percentageForEachLine);
            float percentageOfLastLine =
                    (percentage - (maxLinesToDrawCompletely * percentageForEachLine)) /
                            percentageForEachLine;
            int linesDrawn = 0;

            if (null == scaledSpark) {
                int scaled = (int) (Math.min(lineHeight, lineWidth) * SCALING_FACTOR);
                scaledSpark = Bitmap.createScaledBitmap(spark, scaled, scaled,
                        true);
            }

            for (Line line : score.getLines()) {
                if (linesDrawn == maxLinesToDrawCompletely) {
                    drawLine(canvas, width, height, line, lineWidth, lineHeight,
                            percentageOfLastLine);
                    break;
                }
                else {
                    drawLine(canvas, width, height, line, lineWidth, lineHeight, 1.0f);
                }
                linesDrawn += 1;
            }
        }
    }

    private void drawLine(Canvas canvas, int width, int height, Line line,
                          float lineWidth, float lineHeight, float percentageOfLastLine) {
        switch (line.lineType) {
            case HORIZONTAL:
                float y = lineHeight + (lineHeight * line.row);
                canvas.drawLine(0, y, width * percentageOfLastLine, y, paint);
                canvas.drawBitmap(scaledSpark, (width * percentageOfLastLine) - lineWidth / 2,
                        (lineHeight * 0.75f) + (lineHeight * line.row),
                        bitmapPaint);
                break;
            case VERTICAL:
                float x = lineWidth + (lineWidth * line.col);
                canvas.drawLine(x, 0, x, height * percentageOfLastLine, paint);
                canvas.drawBitmap(scaledSpark, (lineWidth * 0.75f) + (lineWidth * line.col),
                        (height * percentageOfLastLine) - lineHeight / 2,
                        bitmapPaint);
                break;
            case NONE:
                break;
        }
    }
}
