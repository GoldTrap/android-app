package com.asb.goldtrap.views;

import android.content.Context;
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
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private long startTime;
    private DotsGameSnapshot dotsGameSnapshot;
    private Listener mListener;
    private Map<GoodiesState, Bitmap> goodiesCollection = new HashMap<>();
    private BoardComponentDrawer pointDrawer;
    private BoardComponentDrawer cellDrawer;
    private BoardComponentDrawer horizontalLineDrawer;
    private BoardComponentDrawer verticalLineDrawer;
    private BoardComponentDrawer goodiesDrawer;
    private AnimatedBoardComponentDrawer lastFilledCellDrawer;
    private AnimatedBoardComponentDrawer lastLineClickedDrawer;
    private Paint bitmapPaint;
    private Paint dotsPaint;
    private Paint firstPlayerCellPaint;
    private Paint secondPlayerCellPaint;
    private Paint firstPlayerLinePaint;
    private Paint secondPlayerLinePaint;

    public DotBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        this.startTime = System.currentTimeMillis();
        this.invalidate();
    }

    private void init() {
        dotsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPlayerCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
                new CellDrawerThatSkipsLastScoredCells(secondPlayerCellPaint, firstPlayerCellPaint);
        horizontalLineDrawer =
                new HorizontalLineDrawerThatSkipsLastSelectedLine(secondPlayerLinePaint,
                        firstPlayerLinePaint);
        verticalLineDrawer = new VerticalLineDrawerThatSkipsLastSelectedLine(secondPlayerLinePaint,
                firstPlayerLinePaint);
        goodiesDrawer = new GoodiesDrawer(bitmapPaint, goodiesCollection);
        lastFilledCellDrawer =
                new LastFilledCellDrawer(secondPlayerCellPaint, firstPlayerCellPaint);
        lastLineClickedDrawer =
                new LastSelectedLineDrawer(secondPlayerLinePaint, firstPlayerLinePaint);

        this.startTime = System.currentTimeMillis();
    }

    public void setColors(int[] colors) {
        if (null != colors && 5 <= colors.length) {
            dotsPaint.setColor(colors[0]);
            firstPlayerCellPaint.setColor(colors[1]);
            secondPlayerCellPaint.setColor(colors[2]);
            firstPlayerLinePaint.setColor(colors[3]);
            secondPlayerLinePaint.setColor(colors[4]);
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

            verticalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            horizontalLineDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            cellDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            lastFilledCellDrawer.onDraw(canvas, width, height, dotsGameSnapshot, elapsedTime,
                    ANIMATION_DURATION);
            lastLineClickedDrawer.onDraw(canvas, width, height, dotsGameSnapshot, elapsedTime,
                    ANIMATION_DURATION);
            goodiesDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            pointDrawer.onDraw(canvas, width, height, dotsGameSnapshot);
            if (elapsedTime < ANIMATION_DURATION) {
                this.postInvalidateDelayed(DELAY_MILLISECONDS);
            }
            else {
                mListener.animationComplete();
            }
        }


    }

    public interface Listener {
        void onLineClick(int row, int col, LineType lineType);

        void animationComplete();
    }

}
