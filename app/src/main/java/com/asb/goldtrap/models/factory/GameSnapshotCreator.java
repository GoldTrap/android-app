package com.asb.goldtrap.models.factory;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.eo.DynamicGoodieData;
import com.asb.goldtrap.models.eo.GoodieData;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by arjun on 31/10/15.
 */
public class GameSnapshotCreator {
    public static final String TAG = GameSnapshotCreator.class.getSimpleName();
    private Random random = new Random();

    public DotsGameSnapshot createGameSnapshot(Level level) {
        DotsGameSnapshot gameSnapshot = null;
        LineState horizontalLines[][] = new LineState[level.getRows() + 1][level.getCols()];
        LineState verticalLines[][] = new LineState[level.getRows()][level.getCols() + 1];
        CellState cells[][] = new CellState[level.getRows()][level.getCols()];
        Set<Goodie> goodies = new HashSet<>(level.getGoodies().size());
        Set<DynamicGoodie> dynamicGoodies = new HashSet<>(level.getDynamicGoodies().size());

        fillHorizontalLinesForEmptyGame(horizontalLines, level.getRows(), level.getCols());
        fillVerticalLinesForEmptyGame(verticalLines, level.getRows(), level.getCols());
        fillCellsForEmptyGame(cells, level.getRows(), level.getCols());
        List<Cell> combinations = getCombinations(level.getRows(), level.getCols());
        fillBlocked(cells, horizontalLines, verticalLines, combinations, level);
        fillGoodies(goodies, combinations, level);
        fillDynamicGoodies(dynamicGoodies, combinations, level);

        gameSnapshot = new DotsGameSnapshot(cells, horizontalLines, verticalLines,
                goodies, dynamicGoodies);
        return gameSnapshot;
    }

    private void fillDynamicGoodies(Set<DynamicGoodie> dynamicGoodies, List<Cell> combinations,
                                    Level level) {
        for (DynamicGoodieData goodieData : level.getDynamicGoodies()) {
            for (int i = 0; i < goodieData.getCount(); i += 1) {
                int index = random.nextInt(combinations.size());
                Cell cell = combinations.remove(index);
                DynamicGoodie goodie =
                        new DynamicGoodie(GoodiesState.DYNAMIC_GOODIE, cell.getRow(), cell.getCol(),
                                goodieData.getValue());
                dynamicGoodies.add(goodie);
            }
        }
    }

    private void fillGoodies(Set<Goodie> goodies, List<Cell> combinations,
                             Level level) {
        for (GoodieData goodieData : level.getGoodies()) {
            for (int i = 0; i < goodieData.getCount(); i += 1) {
                int index = random.nextInt(combinations.size());
                Cell cell = combinations.remove(index);
                Goodie goodie = new Goodie(goodieData.getType(), cell.getRow(), cell.getCol());
                goodies.add(goodie);
            }
        }
    }

    private void fillBlocked(CellState[][] cells, LineState[][] horizontalLines,
                             LineState[][] verticalLines, List<Cell> combinations, Level level) {
        for (int i = 0; i < level.getBlocked(); i += 1) {
            int index = random.nextInt(combinations.size());
            Cell cell = combinations.remove(index);
            cells[cell.getRow()][cell.getCol()] = CellState.BLOCKED;
            horizontalLines[cell.getRow()][cell.getCol()] = LineState.BLOCKED;
            horizontalLines[cell.getRow() + 1][cell.getCol()] = LineState.BLOCKED;
            verticalLines[cell.getRow()][cell.getCol()] = LineState.BLOCKED;
            verticalLines[cell.getRow()][cell.getCol() + 1] = LineState.BLOCKED;
        }
    }

    private List<Cell> getCombinations(int rows, int cols) {
        List<Cell> combinations = new LinkedList<>();
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                combinations.add(new Cell(CellState.FREE, i, j));
            }
        }
        return combinations;
    }

    private void fillCellsForEmptyGame(CellState[][] cells,
                                       int rows, int cols) {
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                cells[i][j] = CellState.FREE;
            }
        }

    }

    private void fillVerticalLinesForEmptyGame(
            LineState[][] verticalLines, int rows, int cols) {
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols + 1; j += 1) {
                verticalLines[i][j] = LineState.FREE;
            }
        }

    }

    private void fillHorizontalLinesForEmptyGame(
            LineState[][] horizontalLines, int rows, int cols) {
        for (int i = 0; i < rows + 1; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                horizontalLines[i][j] = LineState.FREE;
            }
        }
    }
}
