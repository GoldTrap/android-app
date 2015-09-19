package com.asb.goldtrap.models.factory;

import android.util.Log;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.Random;

public class DotsGameFactory {

    public static DotsGameSnapshot createEmptyGameSnapshot() {
        DotsGameSnapshot gameSnapshot = null;
        Random random = new Random();
        int rows = random.nextInt(8) + 1;
        int cols = random.nextInt(8) + 1;

        Log.v("DotsGameFactory", "rows: " + rows + ", cols: " + cols);
        LineState horizontalLines[][] = new LineState[rows + 1][cols];
        LineState verticalLines[][] = new LineState[rows][cols + 1];
        CellState cells[][] = new CellState[rows][cols];
        GoodiesState goodies[][] = new GoodiesState[rows][cols];

        fillHorizontalLinesForEmptyGame(horizontalLines, rows, cols);
        fillVerticalLinesForEmptyGame(verticalLines, rows, cols);
        fillCellsForEmptyGame(cells, rows, cols);
        fillGoodies(goodies, (rows * cols) / 3, rows, cols);

        gameSnapshot = new DotsGameSnapshot(cells, horizontalLines, verticalLines,
                goodies);
        return gameSnapshot;
    }

    public static DotsGameSnapshot createGameSnapshot(int rows, int cols, int goodiesCount) {
        DotsGameSnapshot gameSnapshot = null;

        Log.v("DotsGameFactory", "rows: " + rows + ", cols: " + cols);
        LineState horizontalLines[][] = new LineState[rows + 1][cols];
        LineState verticalLines[][] = new LineState[rows][cols + 1];
        CellState cells[][] = new CellState[rows][cols];
        GoodiesState goodies[][] = new GoodiesState[rows][cols];

        fillHorizontalLinesForEmptyGame(horizontalLines, rows, cols);
        fillVerticalLinesForEmptyGame(verticalLines, rows, cols);
        fillCellsForEmptyGame(cells, rows, cols);
        fillGoodies(goodies, goodiesCount, rows, cols);

        gameSnapshot = new DotsGameSnapshot(cells, horizontalLines, verticalLines,
                goodies);
        return gameSnapshot;
    }

    private static void fillGoodies(GoodiesState goodies[][], int goodiesCount,
                                    int rows, int cols) {
        Random random = new Random();
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                goodies[i][j] = GoodiesState.NOTHING;
            }
        }
        int max = rows * cols;
        for (int i = 0; i < goodiesCount; i += 1) {
            int rd = random.nextInt(max);
            int r = rd / cols;
            int c = rd % cols;
            goodies[r][c] = GoodiesState.ONE_K;
        }
    }

    private static void fillCells(CellState[][] cells, Random random, int rows,
                                  int cols) {
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                int ctr = random.nextInt(3);
                switch (ctr) {
                    case 0:
                        cells[i][j] = CellState.FREE;
                        break;
                    case 1:
                        cells[i][j] = CellState.PLAYER;
                        break;
                    case 2:
                        cells[i][j] = CellState.AI;
                        break;
                }
            }
        }

    }

    private static void fillCellsForEmptyGame(CellState[][] cells,
                                              int rows, int cols) {
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                cells[i][j] = CellState.FREE;
            }
        }

    }

    private static void fillVerticalLines(LineState[][] verticalLines,
                                          Random random, int rows, int cols) {
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols + 1; j += 1) {
                int ctr = random.nextInt(3);
                switch (ctr) {
                    case 0:
                        verticalLines[i][j] = LineState.FREE;
                        break;
                    case 1:
                        verticalLines[i][j] = LineState.PLAYER;
                        break;
                    case 2:
                        verticalLines[i][j] = LineState.AI;
                        break;
                }
            }
        }

    }

    private static void fillVerticalLinesForEmptyGame(
            LineState[][] verticalLines, int rows, int cols) {
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols + 1; j += 1) {
                verticalLines[i][j] = LineState.FREE;
            }
        }

    }

    private static void fillHorizontalLines(LineState[][] horizontalLines,
                                            Random random, int rows, int cols) {
        for (int i = 0; i < rows + 1; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                int ctr = random.nextInt(3);
                switch (ctr) {
                    case 0:
                        horizontalLines[i][j] = LineState.FREE;
                        break;
                    case 1:
                        horizontalLines[i][j] = LineState.PLAYER;
                        break;
                    case 2:
                        horizontalLines[i][j] = LineState.AI;
                        break;
                }
            }
        }
    }

    private static void fillHorizontalLinesForEmptyGame(
            LineState[][] horizontalLines, int rows, int cols) {
        for (int i = 0; i < rows + 1; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                horizontalLines[i][j] = LineState.FREE;
            }
        }
    }
}
