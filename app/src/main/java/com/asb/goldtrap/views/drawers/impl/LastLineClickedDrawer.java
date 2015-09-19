package com.asb.goldtrap.views.drawers.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;
import com.asb.goldtrap.views.drawers.AnimatedBoardComponentDrawer;

/**
 * Created by arjun on 17/09/15.
 */
public class LastLineClickedDrawer implements AnimatedBoardComponentDrawer {
    private Paint dotsPaint;
    private Paint aiPaint;
    private Paint playerPaint;

    public LastLineClickedDrawer(Paint dotsPaint, Paint aiPaint, Paint playerPaint) {
        this.dotsPaint = dotsPaint;
        this.aiPaint = aiPaint;
        this.playerPaint = playerPaint;
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain, long elapsedTime,
                       long animationDuration) {

        float percentage = (float) elapsedTime / (float) animationDuration;
        if (percentage > 1) {
            percentage = 1;
        }

        int row = brain.getLastClickedRow();
        int col = brain.getLastClickedCol();
        int cols = brain.getHorizontalLines()[0].length + 1;
        int rows = brain.getHorizontalLines().length;
        float lineWidth = width / (cols);
        float lineHeight = height / (rows);

        float x = lineWidth / 2;
        float y = lineHeight / 2;
        x += col * lineWidth;
        y += row * lineHeight;
        float pointRadius = Math.min(lineHeight, lineWidth) * 0.1f;
        LineType lastLineType = brain.getLastClickedLineType();
        if (LineType.NONE != lastLineType) {
            Paint paint = resolvePaint(brain.getLastClickedLineState());

            switch (lastLineType) {
                case HORIZONTAL:
                    canvas.drawLine(x, y, x + (lineWidth * percentage), y, paint);
                    canvas.drawCircle(x + lineWidth, y, pointRadius, dotsPaint);
                    break;
                case NONE:
                    break;
                case VERTICAL:
                    canvas.drawLine(x, y, x, y + (lineHeight * percentage), paint);
                    canvas.drawCircle(x, y + lineHeight, pointRadius, dotsPaint);
                    break;
                default:
                    break;

            }
            canvas.drawCircle(x, y, pointRadius, dotsPaint);
        }
    }

    private Paint resolvePaint(LineState lineState) {
        Paint paint = null;
        switch (lineState) {
            case AI:
                paint = aiPaint;
                break;
            case PLAYER:
                paint = playerPaint;
                break;
            case FREE:
                break;
            default:
                break;
        }
        return paint;
    }
}
