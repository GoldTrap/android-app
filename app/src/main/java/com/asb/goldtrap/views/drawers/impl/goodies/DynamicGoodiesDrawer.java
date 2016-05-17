package com.asb.goldtrap.views.drawers.impl.goodies;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

import java.util.Set;

/**
 * Created by arjun on 26/10/15.
 */
public class DynamicGoodiesDrawer implements BoardComponentDrawer {

    public static final float SCALING_FACTOR = 0.85f;
    public static final float PADDING_FACTOR = 0.15f;
    private Paint bitmapPaintEmpty;
    private Paint bitmapPaintFirst;
    private Paint bitmapPaintSecond;

    public DynamicGoodiesDrawer(Paint bitmapPaintEmpty, Paint bitmapPaintFirst,
                                Paint bitmapPaintSecond) {
        this.bitmapPaintEmpty = bitmapPaintEmpty;
        this.bitmapPaintFirst = bitmapPaintFirst;
        this.bitmapPaintSecond = bitmapPaintSecond;
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain) {
        int cols = brain.getHorizontalLines()[0].length + 1;
        int rows = brain.getHorizontalLines().length;
        float lineWidth = width / (cols);
        float lineHeight = height / (rows);
        // cells
        Set<DynamicGoodie> goodies = brain.getDynamicGoodies();

        for (DynamicGoodie goodie : goodies) {
            if (GoodiesState.NOTHING != goodie.getGoodiesState()) {
                float x = (lineWidth / 2) + lineWidth * goodie.getCol();
                float y = (lineHeight / 2) + lineHeight * goodie.getRow();
                if (goodie.getGoodiesState() == GoodiesState.DYNAMIC_GOODIE) {
                    Paint paint = resolvePaint(brain.getCells()[goodie.getRow()][goodie.getCol()]);
                    drawDynamicGoodie(canvas, lineWidth, lineHeight, goodie, x, y, paint);
                }
            }
        }
    }

    private Paint resolvePaint(CellState cellState) {
        Paint paint = null;
        switch (cellState) {
            case FREE:
            case BLOCKED:
                paint = bitmapPaintEmpty;
                break;
            case PLAYER:
                paint = bitmapPaintFirst;
                break;
            case SECONDARY_PLAYER:
                paint = bitmapPaintSecond;
                break;
        }
        return paint;
    }


    private void drawDynamicGoodie(Canvas canvas, float lineWidth, float lineHeight,
                                   DynamicGoodie goodie,
                                   float x, float y, Paint paint) {
        int widthScaled = (int) (lineWidth * SCALING_FACTOR);
        int heightScaled = (int) (lineHeight * SCALING_FACTOR);
        int paddingWidth = (int) (lineWidth * PADDING_FACTOR);
        float bHStart = (lineHeight - heightScaled) / 2;
        float bWStart = (lineWidth - widthScaled) / 2;
        int left = (int) (x + bWStart);
        int top = (int) (y + bHStart);
        int right = left + widthScaled;
        int bottom = top + heightScaled;
        int noOfDigits = getNumberOfDigits(Math.abs(goodie.getDisplayValue()));
        if (0 == noOfDigits) {
            noOfDigits = 1;
        }
        float textSize = widthScaled / noOfDigits;

        paint.setTextSize(textSize);
        canvas.drawText(String.valueOf(goodie.getDisplayValue()),
                left + paddingWidth, (top + bottom) / 2 + textSize / 3, paint);
    }

    private int getNumberOfDigits(int displayValue) {
        int numberOfDigits = 0;
        while (displayValue > 0) {
            displayValue /= 10;
            numberOfDigits += 1;
        }
        return numberOfDigits;
    }
}
