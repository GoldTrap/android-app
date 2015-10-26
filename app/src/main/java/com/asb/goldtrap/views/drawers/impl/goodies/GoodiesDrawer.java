package com.asb.goldtrap.views.drawers.impl.goodies;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

import java.util.Map;
import java.util.Set;

/**
 * Created by arjun on 17/09/15.
 */
public class GoodiesDrawer implements BoardComponentDrawer {

    public static final float SCALING_FACTOR = 0.70f;
    public static final float PADDING_FACTOR = (1 - SCALING_FACTOR) / 2;
    private Paint bitmapPaint;
    private Map<GoodiesState, Bitmap> goodieCollection;
    private Rect destinationRect;

    public GoodiesDrawer(Paint bitmapPaint,
                         Map<GoodiesState, Bitmap> goodieCollection) {
        this.bitmapPaint = bitmapPaint;
        this.goodieCollection = goodieCollection;
        destinationRect = new Rect(0, 0, 0, 0);
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain) {
        int cols = brain.getHorizontalLines()[0].length + 1;
        int rows = brain.getHorizontalLines().length;
        float lineWidth = width / (cols);
        float lineHeight = height / (rows);
        // cells
        Set<Goodie> goodies = brain.getGoodies();

        for (Goodie goodie : goodies) {
            if (GoodiesState.NOTHING != goodie.getGoodiesState()) {
                float x = (lineWidth / 2) + lineWidth * goodie.getCol();
                float y = (lineHeight / 2) + lineHeight * goodie.getRow();
                drawGoodie(canvas, lineWidth, lineHeight, goodie, x, y);
            }
        }
    }

    private void drawGoodie(Canvas canvas, float lineWidth, float lineHeight, Goodie goodie,
                            float x, float y) {
        int scaled = (int) (Math.min(lineHeight, lineWidth) * SCALING_FACTOR);
        float bHStart = (lineHeight - scaled) / 2;
        float bWStart = (lineWidth - scaled) / 2;
        int left = (int) (x + bWStart);
        int top = (int) (y + bHStart);
        int right = left + scaled;
        int bottom = top + scaled;
        destinationRect.set(left, top, right, bottom);
        Bitmap goodieBitmap = goodieCollection.get(goodie.getGoodiesState());
        canvas.drawBitmap(goodieBitmap, null, destinationRect, bitmapPaint);
    }

}
