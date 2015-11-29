package com.asb.goldtrap.views;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.asb.goldtrap.views.drawers.impl.goodies.DynamicGoodiesDrawer;
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
    private final int gameBoardType;
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
    private BoardComponentDrawer dynamicGoodiesDrawer;
    private AnimatedBoardComponentDrawer achievementsDrawer;
    private Paint bitmapPaint;
    private Paint dotsPaint;
    private Paint firstPlayerCellPaint;
    private Paint secondPlayerCellPaint;
    private Paint blockedCellPaint;
    private Paint firstPlayerLinePaint;
    private Paint secondPlayerLinePaint;
    private Paint blockedLinePaint;
    private Paint achievementsPaint;

    public GameCompleteDotBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray =
                context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameBoard, 0, 0);
        gameBoardType = typedArray.getInt(R.styleable.GameBoard_board_style, 1);
        typedArray.recycle();
        init();
    }

    private void init() {
        dotsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPlayerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockedCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPlayerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        achievementsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int[] colors = getResources().getIntArray(R.array.default_game_complete_theme);
        setColors(colors);

        Bitmap coins = BitmapFactory.decodeResource(getResources(), R.drawable.coins);
        spark = BitmapFactory.decodeResource(getResources(), R.drawable.spark);
        goodiesCollection.put(GoodiesState.ONE_K, coins);
        Bitmap diamond = BitmapFactory.decodeResource(getResources(), R.drawable.diamond);
        goodiesCollection.put(GoodiesState.DIAMOND, diamond);

        // Drawers
        pointDrawer = new PointDrawer(dotsPaint);
        cellDrawer =
                new CellDrawer(secondPlayerCellPaint, firstPlayerCellPaint, blockedCellPaint);
        horizontalLineDrawer =
                new HorizontalLineDrawer(secondPlayerLinePaint,
                        firstPlayerLinePaint, blockedLinePaint);
        verticalLineDrawer = new VerticalLineDrawer(secondPlayerLinePaint,
                firstPlayerLinePaint, blockedLinePaint);
        goodiesDrawer = new GoodiesDrawer(bitmapPaint, goodiesCollection);
        dynamicGoodiesDrawer = new DynamicGoodiesDrawer(bitmapPaint);

        achievementsDrawer = new AchievementsDrawer(achievementsPaint, bitmapPaint, spark);
        this.startTime = System.currentTimeMillis();

    }

    public void setColors(int[] colors) {
        int index = 0;
        if (null != colors && 8 <= colors.length) {
            dotsPaint.setColor(colors[index++]);
            firstPlayerCellPaint.setColor(colors[index++]);
            secondPlayerCellPaint.setColor(colors[index++]);
            blockedCellPaint.setColor(colors[index++]);
            firstPlayerLinePaint.setColor(colors[index++]);
            secondPlayerLinePaint.setColor(colors[index++]);
            blockedLinePaint.setColor(colors[index++]);
            bitmapPaint.setColor(colors[index++]);
            achievementsPaint.setColor(colors[index]);
        }
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
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int minSize = Math.min(widthSize, heightSize);
        switch (gameBoardType) {
            case 1:
                setMeasuredDimension(minSize, minSize);
                break;
            case 2:
                setMeasuredDimension(minSize, minSize * 4 / 3);
                break;
            case 3:
                setMeasuredDimension(minSize * 4 / 3, minSize);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != dotsGameSnapshot) {
            int height = this.getMeasuredHeight();
            int width = this.getMeasuredWidth();
            long elapsedTime = System.currentTimeMillis() - startTime;
            long animationDuration =
                    (dotsGameSnapshot.getScore().getHorizontalLines().size() +
                            dotsGameSnapshot.getScore().getVerticalLines().size()) *
                            ANIMATION_DURATION;

            verticalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            horizontalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            cellDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            goodiesDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            dynamicGoodiesDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
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
