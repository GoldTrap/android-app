package com.asb.goldtrap.views.drawers;

import android.graphics.Canvas;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;

/**
 * Draws the components of Board
 * Created by arjun on 17/09/15.
 */
public interface BoardComponentDrawer {
    /**
     * API to draw the component
     *
     * @param canvas Canvas to draw
     * @param width  board width
     * @param height board height
     * @param brain  game snapshot
     */
    void onDraw(Canvas canvas, int width, int height, DotsGameSnapshot brain);
}
