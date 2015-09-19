package com.asb.goldtrap.views.drawers.impl;

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

    public AbstractLineDrawer(Paint aiPaint, Paint playerPaint) {
        this.aiPaint = aiPaint;
        this.playerPaint = playerPaint;
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
                if (null != paint) {
                    drawLine(canvas, brain, lineWidth, lineHeight, i, j, x, y, paint);
                }
                x += lineWidth;
            }
            x = lineWidth / 2;
            y += lineHeight;
        }
    }

    protected abstract void drawLine(Canvas canvas, DotsGameSnapshot brain, float lineWidth,
                                     float lineHeight, int row, int col, float x,
                                     float y, Paint paint);

    private Paint resolvePaint(LineState line) {
        Paint paint = null;
        switch (line) {
            case AI:
                paint = aiPaint;
                break;
            case FREE:
                break;
            case PLAYER:
                paint = playerPaint;
                break;
            default:
                break;
        }
        return paint;
    }

    protected abstract LineState[][] getLines(DotsGameSnapshot brain);
}
