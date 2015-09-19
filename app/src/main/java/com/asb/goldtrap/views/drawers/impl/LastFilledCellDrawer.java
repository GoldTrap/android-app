package com.asb.goldtrap.views.drawers.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.views.drawers.AnimatedBoardComponentDrawer;

import java.util.List;

/**
 * Created by arjun on 17/09/15.
 */
public class LastFilledCellDrawer implements AnimatedBoardComponentDrawer {

    private Paint aiPaint;
    private Paint playerPaint;

    public LastFilledCellDrawer(Paint aiPaint, Paint playerPaint) {
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
        int cols = brain.getHorizontalLines()[0].length + 1;
        int rows = brain.getHorizontalLines().length;
        float lineWidth = width / (cols);
        float lineHeight = height / (rows);

        List<Cell> lastScoredCells = brain.getLastScoredCells();
        if (null != lastScoredCells && !lastScoredCells.isEmpty()) {
            for (Cell cell : lastScoredCells) {
                int col = cell.getLastScoreCellCol();
                int row = cell.getLastScoredCellRow();
                float x = lineWidth / 2;
                float y = lineHeight / 2;
                x += col * lineWidth;
                y += row * lineHeight;
                Paint paint = resolvePaint(cell.getCellState());

                if (null != paint) {
                    float xDelta = lineWidth * 0.1f;
                    float yDelta = lineHeight * 0.1f;
                    canvas.drawRect(x + xDelta, y + yDelta, (x
                            + (lineWidth * percentage) - xDelta), (y
                            + (lineHeight * percentage) - yDelta), paint);
                }
            }

        }
    }

    private Paint resolvePaint(CellState cell) {
        Paint paint = null;
        switch (cell) {
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
}
