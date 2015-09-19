package com.asb.goldtrap.views.drawers.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arjun on 17/09/15.
 */
public class GoodiesDrawer implements BoardComponentDrawer {

    public static final float SCALING_FACTOR = 0.70f;
    private Paint bitmapPaint;
    private Map<GoodiesState, Bitmap> goodieCollection;
    private Map<GoodiesState, Bitmap> scaledGoodieCollection = new HashMap<>();

    public GoodiesDrawer(Paint bitmapPaint,
                         Map<GoodiesState, Bitmap> goodieCollection) {
        this.bitmapPaint = bitmapPaint;
        this.goodieCollection = goodieCollection;
    }

    @Override
    public void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain) {
        int cols = brain.getHorizontalLines()[0].length + 1;
        int rows = brain.getHorizontalLines().length;
        float lineWidth = width / (cols);
        float lineHeight = height / (rows);
        // cells
        GoodiesState goodies[][] = brain.getGoodies();
        float x = lineWidth / 2;
        float y = lineHeight / 2;

        int scaled = (int) (Math.min(lineHeight, lineWidth) * SCALING_FACTOR);

        for (int i = 0; i < goodies.length; i += 1) {
            GoodiesState goodiesStates[] = goodies[i];
            for (int j = 0; j < goodiesStates.length; j += 1) {
                if (GoodiesState.NOTHING != goodiesStates[j]) {
                    Bitmap scaledGoodie = scaledGoodieCollection.get(goodiesStates[j]);
                    if (null == scaledGoodie) {
                        Bitmap goodie = goodieCollection.get(goodiesStates[j]);
                        scaledGoodie = Bitmap.createScaledBitmap(goodie, scaled, scaled,
                                true);
                        scaledGoodieCollection.put(goodiesStates[j], scaledGoodie);

                    }
                    int bHStart = (int) (lineHeight - scaledGoodie.getHeight()) / 2;
                    int bWStart = (int) (lineWidth - scaledGoodie.getWidth()) / 2;
                    canvas.drawBitmap(scaledGoodie, x + bWStart, y + bHStart,
                            bitmapPaint);
                }
                x += lineWidth;
            }
            x = lineWidth / 2;
            y += lineHeight;
        }

    }
}
