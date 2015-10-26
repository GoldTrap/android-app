package com.asb.goldtrap.models.factory;

import android.util.Log;

import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        Set<Goodie> goodies = new HashSet<>();
        Set<DynamicGoodie> dynamicGoodies = new HashSet<>();

        fillHorizontalLinesForEmptyGame(horizontalLines, rows, cols);
        fillVerticalLinesForEmptyGame(verticalLines, rows, cols);
        fillCellsForEmptyGame(cells, rows, cols);
        fillGoodies(cells, goodies, (rows * cols) / 3, rows, cols);

        gameSnapshot = new DotsGameSnapshot(cells, horizontalLines, verticalLines,
                goodies, dynamicGoodies);
        return gameSnapshot;
    }

    public static DotsGameSnapshot createGameSnapshot(int rows, int cols, int goodiesCount,
                                                      int blockedCount) {
        DotsGameSnapshot gameSnapshot = null;

        Log.v("DotsGameFactory", "rows: " + rows + ", cols: " + cols);
        LineState horizontalLines[][] = new LineState[rows + 1][cols];
        LineState verticalLines[][] = new LineState[rows][cols + 1];
        CellState cells[][] = new CellState[rows][cols];
        Set<Goodie> goodies = new HashSet<>(goodiesCount);
        Set<DynamicGoodie> dynamicGoodies = new HashSet<>(goodiesCount);

        fillHorizontalLinesForEmptyGame(horizontalLines, rows, cols);
        fillVerticalLinesForEmptyGame(verticalLines, rows, cols);
        fillCellsForEmptyGame(cells, rows, cols);
        fillBlocked(cells, horizontalLines, verticalLines, rows, cols, blockedCount);
        fillGoodies(cells, goodies, rows, cols, goodiesCount / 2);
        fillDynamicGoodies(cells, dynamicGoodies, rows, cols, goodiesCount / 2);

        gameSnapshot = new DotsGameSnapshot(cells, horizontalLines, verticalLines,
                goodies, dynamicGoodies);
        return gameSnapshot;
    }

    private static void fillDynamicGoodies(CellState[][] cells, Set<DynamicGoodie> goodies,
                                           int rows,
                                           int cols, int goodiesCount) {
        Random random = new Random();
        int max = rows * cols;
        for (int i = 0; i < goodiesCount; i += 1) {
            int rd = random.nextInt(max);
            int r = rd / cols;
            int c = rd % cols;
            if (cells[r][c] != CellState.FREE) {
                continue;
            }
            DynamicGoodie goodie =
                    new DynamicGoodie(GoodiesState.DYNAMIC_GOODIE, r, c, random.nextInt(15) + 1);
            goodies.add(goodie);
        }
    }

    private static void fillGoodies(CellState[][] cells, Set<Goodie> goodies,
                                    int rows, int cols, int goodiesCount) {
        Random random = new Random();
        int max = rows * cols;
        for (int i = 0; i < goodiesCount; i += 1) {
            int rd = random.nextInt(max);
            int r = rd / cols;
            int c = rd % cols;
            if (cells[r][c] != CellState.FREE) {
                continue;
            }
            Goodie goodie = new Goodie(GoodiesState.ONE_K, r, c);
            goodies.add(goodie);
        }
    }

    private static void fillBlocked(CellState[][] cells, LineState[][] horizontalLines,
                                    LineState[][] verticalLines,
                                    int rows, int cols, int blockedCount) {
        Random random = new Random();
        int max = rows * cols;
        for (int i = 0; i < blockedCount; i += 1) {
            int rd = random.nextInt(max);
            int r = rd / cols;
            int c = rd % cols;
            cells[r][c] = CellState.BLOCKED;
            horizontalLines[r][c] = LineState.BLOCKED;
            horizontalLines[r + 1][c] = LineState.BLOCKED;
            verticalLines[r][c] = LineState.BLOCKED;
            verticalLines[r][c + 1] = LineState.BLOCKED;
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
