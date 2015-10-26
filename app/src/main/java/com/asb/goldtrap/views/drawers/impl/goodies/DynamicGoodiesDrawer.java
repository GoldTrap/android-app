package com.asb.goldtrap.views.drawers.impl.goodies;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

import java.util.Map;
import java.util.Set;

/**
 * Created by arjun on 26/10/15.
 */
public class DynamicGoodiesDrawer implements BoardComponentDrawer {

    public static final float SCALING_FACTOR = 0.70f;
    public static final float PADDING_FACTOR = (1 - SCALING_FACTOR) / 2;
    private Paint bitmapPaint;

    public DynamicGoodiesDrawer(Paint bitmapPaint) {
        this.bitmapPaint = bitmapPaint;
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
                    drawDynamicGoodie(canvas, lineWidth, lineHeight, goodie, x, y);
                }
            }
        }
    }


    private void drawDynamicGoodie(Canvas canvas, float lineWidth, float lineHeight,
                                   DynamicGoodie goodie,
                                   float x, float y) {
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
        bitmapPaint.setTextSize(textSize);
        canvas.drawText(String.valueOf(goodie.getDisplayValue()),
                left + paddingWidth, (top + bottom) / 2 + textSize / 2, bitmapPaint);
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
