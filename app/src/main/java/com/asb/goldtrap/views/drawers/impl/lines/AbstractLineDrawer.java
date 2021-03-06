package com.asb.goldtrap.views.drawers.impl.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

/**
 * Created by arjun on 17/09/15.
 */
public abstract class AbstractLineDrawer implements BoardComponentDrawer {

    private Paint aiPaint;
    private Paint playerPaint;
    private Paint blockedPaint;

    public AbstractLineDrawer(Paint aiPaint, Paint playerPaint, Paint blockedPaint) {
        this.aiPaint = aiPaint;
        this.playerPaint = playerPaint;
        this.blockedPaint = blockedPaint;
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain) {
        int cols = brain.getHorizontalLines()[0].length + 1;
        int rows = brain.getHorizontalLines().length;
        float lineWidth = width / (cols);
        float lineHeight = height / (rows);

        float x = lineWidth / 2;
        float y = lineHeight / 2;
        LineState[][] lines = getLines(brain);
        for (int i = 0; i < lines.length; i += 1) {
            LineState line[] = lines[i];
            for (int j = 0; j < line.length; j += 1) {
                Paint paint = resolvePaint(line[j]);
                if (shouldDrawLine(paint, brain, i, j)) {
                    drawLine(canvas, lineWidth, lineHeight, x, y, paint);
                }
                x += lineWidth;
            }
            x = lineWidth / 2;
            y += lineHeight;
        }
    }

    protected abstract boolean shouldDrawLine(Paint paint, DotsGameSnapshot brain, int row,
                                              int col);

    protected abstract void drawLine(Canvas canvas, float lineWidth,
                                     float lineHeight, float x,
                                     float y, Paint paint);

    private Paint resolvePaint(LineState line) {
        Paint paint = null;
        switch (line) {
            case SECONDARY_PLAYER:
                paint = aiPaint;
                break;
            case FREE:
                break;
            case PLAYER:
                paint = playerPaint;
                break;
            case BLOCKED:
                paint = blockedPaint;
                break;
            default:
                break;
        }
        return paint;
    }

    protected abstract LineState[][] getLines(DotsGameSnapshot brain);
}
