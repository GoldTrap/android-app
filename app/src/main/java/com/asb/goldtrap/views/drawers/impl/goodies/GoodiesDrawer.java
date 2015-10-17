package com.asb.goldtrap.views.drawers.impl.goodies;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Set<Goodie> goodies = brain.getGoodies();

        int scaled = (int) (Math.min(lineHeight, lineWidth) * SCALING_FACTOR);

        for (Goodie goodie : goodies) {
            if (GoodiesState.NOTHING != goodie.getGoodiesState()) {
                float x = (lineWidth / 2) + lineWidth * goodie.getCol();
                float y = (lineHeight / 2) + lineHeight * goodie.getRow();
                Bitmap scaledGoodie = scaledGoodieCollection.get(goodie.getGoodiesState());
                if (null == scaledGoodie) {
                    Bitmap goodieBitmap = goodieCollection.get(goodie.getGoodiesState());
                    scaledGoodie = Bitmap.createScaledBitmap(goodieBitmap, scaled, scaled,
                            true);
                    scaledGoodieCollection.put(goodie.getGoodiesState(), scaledGoodie);

                }
                int bHStart = (int) (lineHeight - scaledGoodie.getHeight()) / 2;
                int bWStart = (int) (lineWidth - scaledGoodie.getWidth()) / 2;
                canvas.drawBitmap(scaledGoodie, x + bWStart, y + bHStart,
                        bitmapPaint);
            }
        }
    }
}
