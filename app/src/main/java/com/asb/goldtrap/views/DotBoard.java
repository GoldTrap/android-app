package com.asb.goldtrap.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.drawers.AnimatedBoardComponentDrawer;
import com.asb.goldtrap.views.drawers.BoardComponentDrawer;
import com.asb.goldtrap.views.drawers.impl.cells.CellDrawerThatSkipsLastScoredCells;
import com.asb.goldtrap.views.drawers.impl.cells.LastFilledCellDrawer;
import com.asb.goldtrap.views.drawers.impl.goodies.DynamicGoodiesDrawer;
import com.asb.goldtrap.views.drawers.impl.goodies.GoodiesDrawer;
import com.asb.goldtrap.views.drawers.impl.lines.HorizontalLineDrawerThatSkipsLastSelectedLine;
import com.asb.goldtrap.views.drawers.impl.lines.LastSelectedLineDrawer;
import com.asb.goldtrap.views.drawers.impl.lines.VerticalLineDrawerThatSkipsLastSelectedLine;
import com.asb.goldtrap.views.drawers.impl.points.PointDrawer;

import java.util.HashMap;
import java.util.Map;

public class DotBoard extends View implements View.OnTouchListener {

    private static final int ONE_SECOND_IN_MILLIS = 1000;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int DELAY_MILLISECONDS = ONE_SECOND_IN_MILLIS / FRAMES_PER_SECOND;
    private static final long ANIMATION_DURATION = 500; // 0.5 seconds
    private long startTime;
    private DotsGameSnapshot dotsGameSnapshot;
    private Listener mListener;
    private Map<GoodiesState, Bitmap> goodiesCollection = new HashMap<>();
    private BoardComponentDrawer pointDrawer;
    private BoardComponentDrawer cellDrawer;
    private BoardComponentDrawer horizontalLineDrawer;
    private BoardComponentDrawer verticalLineDrawer;
    private BoardComponentDrawer goodiesDrawer;
    private BoardComponentDrawer dynamicGoodiesDrawer;
    private AnimatedBoardComponentDrawer lastFilledCellDrawer;
    private AnimatedBoardComponentDrawer lastLineClickedDrawer;
    private boolean animationRequested = false;
    private int dotBoardType;
    private Paint bitmapPaint;
    private Paint dotsPaint;
    private Paint firstPlayerCellPaint;
    private Paint secondPlayerCellPaint;
    private Paint blockedCellPaint;
    private Paint blockedLinePaint;
    private Paint firstPlayerLinePaint;
    private Paint secondPlayerLinePaint;

    public DotBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray =
                context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameBoard, 0, 0);
        dotBoardType = typedArray.getInt(R.styleable.GameBoard_board_style, 1);
        typedArray.recycle();
        this.setOnTouchListener(this);
        init();
    }

    public Listener getmListener() {
        return mListener;
    }

    public void setmListener(
            Listener mListener) {
        this.mListener = mListener;
    }

    public void setGameSnapShot(DotsGameSnapshot gameSnapshot) {
        this.dotsGameSnapshot = gameSnapshot;
        requestRedraw();
    }

    public void requestRedraw() {
        this.animationRequested = true;
        this.startTime = System.currentTimeMillis();
        this.invalidate();
    }

    private void init() {
        dotsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockedCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPlayerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPlayerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int[] colors = getResources().getIntArray(R.array.default_theme);
        setColors(colors);

        Bitmap coins = BitmapFactory.decodeResource(getResources(), R.drawable.coins);
        goodiesCollection.put(GoodiesState.ONE_K, coins);

        // Drawers
        pointDrawer = new PointDrawer(dotsPaint);
        cellDrawer =
                new CellDrawerThatSkipsLastScoredCells(secondPlayerCellPaint, firstPlayerCellPaint,
                        blockedCellPaint);
        horizontalLineDrawer =
                new HorizontalLineDrawerThatSkipsLastSelectedLine(secondPlayerLinePaint,
                        firstPlayerLinePaint, blockedLinePaint);
        verticalLineDrawer = new VerticalLineDrawerThatSkipsLastSelectedLine(secondPlayerLinePaint,
                firstPlayerLinePaint, blockedLinePaint);
        goodiesDrawer = new GoodiesDrawer(bitmapPaint, goodiesCollection);
        dynamicGoodiesDrawer = new DynamicGoodiesDrawer(bitmapPaint);
        lastFilledCellDrawer =
                new LastFilledCellDrawer(secondPlayerCellPaint, firstPlayerCellPaint);
        lastLineClickedDrawer =
                new LastSelectedLineDrawer(secondPlayerLinePaint, firstPlayerLinePaint);

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
            bitmapPaint.setColor(colors[index]);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            // When user touches the screen
            case MotionEvent.ACTION_DOWN:
                findLineType(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                v.performClick();
                break;
        }
        return true;
    }

    private void findLineType(float clickedX, float clickedY) {
        int row = 0;
        int col = 0;
        LineType lineType;
        int height = this.getMeasuredHeight();
        int width = this.getMeasuredWidth();
        LineState horizontalLines[][] = dotsGameSnapshot
                .getHorizontalLines();

        int cols = horizontalLines[0].length + 1;
        int rows = horizontalLines.length;

        float lineWidth = width / (cols);
        float lineHeight = height / (rows);
        float x = clickedX - (lineWidth / 2);
        float y = clickedY - (lineHeight / 2);

        float xIndex = x / lineWidth;
        float yIndex = y / lineHeight;

        int xTopLeft = (int) Math.floor(xIndex);
        int yTopLeft = (int) Math.floor(yIndex);

        float distToTop = y - (yTopLeft * lineHeight);
        float distToBottom = ((yTopLeft + 1) * lineHeight) - y;

        float distToLeft = x - (xTopLeft * lineWidth);
        float distToRight = ((xTopLeft + 1) * lineWidth) - x;

        if (distToTop < distToBottom && distToTop < distToLeft
                && distToTop < distToRight) {
            row = yTopLeft;
            col = xTopLeft;
            lineType = LineType.HORIZONTAL;
        }
        else if (distToBottom < distToTop && distToBottom < distToLeft
                && distToBottom < distToRight) {
            row = yTopLeft + 1;
            col = xTopLeft;
            lineType = LineType.HORIZONTAL;
        }
        else if (distToLeft < distToTop && distToLeft < distToBottom
                && distToLeft < distToRight) {
            row = yTopLeft;
            col = xTopLeft;
            lineType = LineType.VERTICAL;
        }
        else {
            row = yTopLeft;
            col = xTopLeft + 1;
            lineType = LineType.VERTICAL;
        }

        if (0 > row || 0 > col) {
            lineType = LineType.NONE;
        }
        if (lineType == LineType.HORIZONTAL && col >= cols - 1) {
            lineType = LineType.NONE;
        }
        if (lineType == LineType.VERTICAL && row >= rows - 1) {
            lineType = LineType.NONE;
        }

        mListener.onLineClick(row, col, lineType);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int minSize = Math.min(widthSize, heightSize);
        switch (dotBoardType) {
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

            verticalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            horizontalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            cellDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            lastFilledCellDrawer.onDraw(canvas, width, height, dotsGameSnapshot, elapsedTime,
                    ANIMATION_DURATION);
            lastLineClickedDrawer.onDraw(canvas, width, height, dotsGameSnapshot, elapsedTime,
                    ANIMATION_DURATION);
            goodiesDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            dynamicGoodiesDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            pointDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            if (animationRequested) {
                if (elapsedTime < ANIMATION_DURATION) {
                    this.postInvalidateDelayed(DELAY_MILLISECONDS);
                }
                else {
                    animationRequested = false;
                    mListener.animationComplete();
                }
            }
        }


    }

    public interface Listener {
        void onLineClick(int row, int col, LineType lineType);

        void animationComplete();
    }

}
