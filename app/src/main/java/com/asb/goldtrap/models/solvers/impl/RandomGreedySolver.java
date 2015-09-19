package com.asb.goldtrap.models.solvers.impl;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by arjun on 21/05/15.
 */
public class RandomGreedySolver implements AISolver {
    protected DotsGameSnapshot dotsGameSnapshot;
    protected List<Line> combinations;
    protected Random random = new Random();

    public RandomGreedySolver(DotsGameSnapshot dotsGameSnapshot,
                              List<Line> combinations) {
        super();
        this.dotsGameSnapshot = dotsGameSnapshot;
        this.combinations = combinations;
    }

    @Override
    public Line getNextLine() {
        Line line;
        if (random.nextBoolean()) {
            line = getNextRandomLine();
        }
        else {
            line = getNextGreedyLine();
        }
        return line;
    }

    protected Line getNextRandomLine() {
        int size = combinations.size();
        int index = random.nextInt(size);
        Line line = combinations.get(index);
        return line;
    }

    protected Line getNextGreedyLine() {
        Line line = null;

        LineState[][] horizontalLines = dotsGameSnapshot.getHorizontalLines();
        LineState[][] verticalLines = dotsGameSnapshot.getVerticalLines();
        List<Line> singleList = new LinkedList<>();
        List<Line> doubleList = new LinkedList<>();

        for (Line combination : combinations) {
            int ctr = 0;
            switch (combination.lineType) {
                case HORIZONTAL:
                    ctr = getFilledLinesForHorizontalLine(horizontalLines,
                            verticalLines, combination.row, combination.col);
                    break;
                case NONE:
                    break;
                case VERTICAL:
                    ctr = getFilledLinesForVerticalLine(horizontalLines,
                            verticalLines, combination.row, combination.col);
                    break;
                default:
                    break;
            }
            switch (ctr) {
                case 1:
                    singleList.add(combination);
                    break;
                case 2:
                    doubleList.add(combination);
                    break;
                case 3:
                    return combination;
            }
        }
        if (!singleList.isEmpty()) {
            line = singleList.get(random.nextInt(singleList.size()));
        }
        else if (!doubleList.isEmpty()) {
            line = doubleList.get(random.nextInt(doubleList.size()));
        }
        else {
            line = combinations.get(random.nextInt(combinations.size()));
        }
        return line;
    }

    protected int getFilledLinesForVerticalLine(LineState[][] horizontalLines,
                                                LineState[][] verticalLines, int row, int col) {
        int cols = horizontalLines[0].length + 1;
        int right = -1;
        int left = -1;
        if (0 == col) {
            right = getFilledLinesForVerticalLine(horizontalLines,
                    verticalLines, row, col, true);
        }
        else if (cols - 1 == col) {
            left = getFilledLinesForVerticalLine(horizontalLines,
                    verticalLines, row, col, false);
        }
        else {
            left = getFilledLinesForVerticalLine(horizontalLines,
                    verticalLines, row, col, false);
            right = getFilledLinesForVerticalLine(horizontalLines,
                    verticalLines, row, col, true);
        }
        return Math.max(left, right);
    }

    private int getFilledLinesForVerticalLine(LineState[][] horizontalLines,
                                              LineState[][] verticalLines, int row, int col,
                                              boolean isRight) {

        int ctr = 0;
        if (isRight) {
            if (horizontalLines[row][col] != LineState.FREE) {
                ctr += 1;
            }
            if (horizontalLines[row + 1][col] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row][col] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row][col + 1] != LineState.FREE) {
                ctr += 1;
            }
        }
        else {
            if (horizontalLines[row][col - 1] != LineState.FREE) {
                ctr += 1;
            }
            if (horizontalLines[row + 1][col - 1] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row][col] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row][col - 1] != LineState.FREE) {
                ctr += 1;
            }
        }

        return ctr;
    }

    protected int getFilledLinesForHorizontalLine(
            LineState[][] horizontalLines, LineState[][] verticalLines,
            int row, int col) {
        int top = -1;
        int bottom = -1;
        int rows = horizontalLines.length;
        if (0 == row) {
            bottom = getFilledLinesForHorizontalLine(horizontalLines,
                    verticalLines, row, col, true);
        }
        else if (rows - 1 == row) {
            top = getFilledLinesForHorizontalLine(horizontalLines,
                    verticalLines, row, col, false);
        }
        else {
            top = getFilledLinesForHorizontalLine(horizontalLines,
                    verticalLines, row, col, false);
            bottom = getFilledLinesForHorizontalLine(horizontalLines,
                    verticalLines, row, col, true);
        }
        return Math.max(top, bottom);
    }

    private int getFilledLinesForHorizontalLine(LineState[][] horizontalLines,
                                                LineState[][] verticalLines, int row, int col,
                                                boolean isDown) {
        int ctr = 0;
        if (isDown) {
            if (horizontalLines[row][col] != LineState.FREE) {
                ctr += 1;
            }
            if (horizontalLines[row + 1][col] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row][col] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row][col + 1] != LineState.FREE) {
                ctr += 1;
            }
        }
        else {
            if (horizontalLines[row][col] != LineState.FREE) {
                ctr += 1;
            }
            if (horizontalLines[row - 1][col] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row - 1][col] != LineState.FREE) {
                ctr += 1;
            }
            if (verticalLines[row - 1][col + 1] != LineState.FREE) {
                ctr += 1;
            }
        }
        return ctr;
    }
}
