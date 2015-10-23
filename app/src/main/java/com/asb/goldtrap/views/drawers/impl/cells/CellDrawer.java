package com.asb.goldtrap.views.drawers.impl.cells;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by arjun on 17/09/15.
 */
public class CellDrawer implements BoardComponentDrawer {

    private Paint aiPaint;
    private Paint playerPaint;
    private Paint blockedPaint;

    public CellDrawer(Paint aiPaint, Paint playerPaint, Paint blockedPaint) {
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
        // cells
        CellState cells[][] = brain.getCells();
        float x = lineWidth / 2;
        float y = lineHeight / 2;
        List<Cell> lastScoredCells = brain.getLastScoredCells();
        Set<String> lookup = new HashSet<>();

        for (Cell lastScoredCell : lastScoredCells) {
            lookup.add(lastScoredCell.getRow() + ", "
                    + lastScoredCell.getCol());
        }
        for (int i = 0; i < cells.length; i += 1) {
            CellState cell[] = cells[i];
            for (int j = 0; j < cell.length; j += 1) {
                Paint paint = resolvePaint(cell[j]);
                float xDelta = lineWidth * 0.15f;
                float yDelta = lineHeight * 0.15f;

                // Draw cell if it's not previously added
                if (shouldPaintTheCell(paint, lookup, i, j)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        canvas.drawRoundRect(x + xDelta, y + yDelta, x + lineWidth
                                - xDelta, y + lineHeight - yDelta, xDelta, yDelta, paint);
                    }
                    else {
                        canvas.drawRect(x + xDelta, y + yDelta, x + lineWidth
                                - xDelta, y + lineHeight - yDelta, paint);
                    }

                }
                x += lineWidth;
            }
            x = lineWidth / 2;
            y += lineHeight;
        }

    }

    protected boolean shouldPaintTheCell(Paint paint, Set<String> lookup, int row, int col) {
        return null != paint;
    }

    private Paint resolvePaint(CellState cellState) {
        Paint paint = null;
        switch (cellState) {
            case AI:
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
}
