package com.asb.goldtrap.views.drawers.impl.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;
import com.asb.goldtrap.views.drawers.AnimatedBoardComponentDrawer;

/**
 * Created by arjun on 17/09/15.
 */
public class LastSelectedLineDrawer implements AnimatedBoardComponentDrawer {
    private Paint aiPaint;
    private Paint playerPaint;

    public LastSelectedLineDrawer(Paint aiPaint, Paint playerPaint) {
        this.aiPaint = aiPaint;
        this.playerPaint = playerPaint;
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain,
                       long elapsedTime,
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
        LineType lastLineType = brain.getLastClickedLineType();
        if (LineType.NONE != lastLineType) {
            Paint paint = resolvePaint(brain.getLastClickedLineState());

            switch (lastLineType) {
                case HORIZONTAL:
                    canvas.drawRect(x, y - lineHeight * 0.05f, x + lineWidth * percentage,
                            y + lineHeight * 0.05f, paint);
                    break;
                case NONE:
                    break;
                case VERTICAL:
                    canvas.drawRect(x - lineHeight * 0.05f, y, x + lineHeight * 0.05f,
                            y + lineHeight * percentage, paint);
                    break;
                default:
                    break;

            }
        }
    }

    private Paint resolvePaint(LineState lineState) {
        Paint paint = null;
        switch (lineState) {
            case SECONDARY_PLAYER:
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
