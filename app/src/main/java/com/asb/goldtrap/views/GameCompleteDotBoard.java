package com.asb.goldtrap.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.views.drawers.AnimatedBoardComponentDrawer;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;
import com.asb.goldtrap.views.drawers.impl.achievements.AchievementsDrawer;
import com.asb.goldtrap.views.drawers.impl.cells.CellDrawer;
import com.asb.goldtrap.views.drawers.impl.goodies.GoodiesDrawer;
import com.asb.goldtrap.views.drawers.impl.lines.HorizontalLineDrawer;
import com.asb.goldtrap.views.drawers.impl.lines.VerticalLineDrawer;
import com.asb.goldtrap.views.drawers.impl.points.PointDrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arjun on 26/09/15.
 */
public class GameCompleteDotBoard extends View {

    private static final int ONE_SECOND_IN_MILLIS = 1000;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int DELAY_MILLISECONDS = ONE_SECOND_IN_MILLIS / FRAMES_PER_SECOND;
    private static final long ANIMATION_DURATION = 500; // 0.5 seconds
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private long startTime;
    private DotsGameSnapshot dotsGameSnapshot;
    private Listener mListener;
    private Map<GoodiesState, Bitmap> goodiesCollection = new HashMap<>();
    private Bitmap spark;
    private BoardComponentDrawer pointDrawer;
    private BoardComponentDrawer cellDrawer;
    private BoardComponentDrawer horizontalLineDrawer;
    private BoardComponentDrawer verticalLineDrawer;
    private BoardComponentDrawer goodiesDrawer;
    private AnimatedBoardComponentDrawer achievementsDrawer;
    private Paint bitmapPaint;
    private Paint dotsPaint;
    private Paint firstPlayerCellPaint;
    private Paint secondPlayerCellPaint;
    private Paint firstPlayerLinePaint;
    private Paint secondPlayerLinePaint;
    private Paint achievementsPaint;

    public GameCompleteDotBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dotsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPlayerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPlayerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        achievementsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int[] colors = getResources().getIntArray(R.array.default_game_complete_theme);
        setColors(colors);

        Bitmap coins = BitmapFactory.decodeResource(getResources(), R.drawable.coins);
        spark = BitmapFactory.decodeResource(getResources(), R.drawable.spark);
        goodiesCollection.put(GoodiesState.ONE_K, coins);

        // Drawers
        pointDrawer = new PointDrawer(dotsPaint);
        cellDrawer =
                new CellDrawer(secondPlayerCellPaint, firstPlayerCellPaint);
        horizontalLineDrawer =
                new HorizontalLineDrawer(secondPlayerLinePaint,
                        firstPlayerLinePaint);
        verticalLineDrawer = new VerticalLineDrawer(secondPlayerLinePaint,
                firstPlayerLinePaint);
        goodiesDrawer = new GoodiesDrawer(bitmapPaint, goodiesCollection);
        achievementsDrawer = new AchievementsDrawer(achievementsPaint, bitmapPaint, spark);
        this.startTime = System.currentTimeMillis();

    }

    public void setColors(int[] colors) {
        dotsPaint.setColor(colors[0]);
        firstPlayerCellPaint.setColor(colors[1]);
        secondPlayerCellPaint.setColor(colors[2]);
        firstPlayerLinePaint.setColor(colors[3]);
        secondPlayerLinePaint.setColor(colors[4]);
        achievementsPaint.setColor(colors[5]);
    }


    public void setGameSnapShot(DotsGameSnapshot gameSnapshot) {
        this.dotsGameSnapshot = gameSnapshot;
        requestRedraw();
    }

    public void requestRedraw() {
        this.startTime = System.currentTimeMillis();
        this.invalidate();
    }

    public void setmListener(
            Listener mListener) {
        this.mListener = mListener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Get size requested and size mode
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height = 0;

        // Determine Width
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(WIDTH, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                width = WIDTH;
                break;
        }

        // Determine Height
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(HEIGHT, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                height = HEIGHT;
                break;
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != dotsGameSnapshot) {
            int height = this.getMeasuredHeight();
            int width = this.getMeasuredWidth();
            long elapsedTime = System.currentTimeMillis() - startTime;
            long animationDuration =
                    dotsGameSnapshot.getScore().getLines().size() * ANIMATION_DURATION;

            verticalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            horizontalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            cellDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            goodiesDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            pointDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            achievementsDrawer.onDraw(canvas, width, height, dotsGameSnapshot, elapsedTime,
                    animationDuration);
            if (elapsedTime < animationDuration) {
                this.postInvalidateDelayed(DELAY_MILLISECONDS);
            }
            else {
                mListener.animationComplete();
            }
        }


    }

    public interface Listener {
        void animationComplete();
    }
}
