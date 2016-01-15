package com.asb.goldtrap.models.conductor.helper.impl;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.conductor.helper.Flipper;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.List;

/**
 * Created by arjun on 15/01/16.
 */
public class FlipperImpl implements Flipper {

    @Override
    public void flipBoard(DotsGameSnapshot dotsGameSnapshot) {
        CellState[][] cells = dotsGameSnapshot.getCells();
        LineState[][] horizontalLines = dotsGameSnapshot.getHorizontalLines();
        LineState[][] verticalLines = dotsGameSnapshot.getVerticalLines();
        for (int i = 0; i < cells.length; i += 1) {
            CellState cell[] = cells[i];
            for (int j = 0; j < cell.length; j += 1) {
                switch (cells[i][j]) {
                    case SECONDARY_PLAYER:
                        cells[i][j] = CellState.PLAYER;
                        break;
                    case FREE:
                        break;
                    case PLAYER:
                        cells[i][j] = CellState.SECONDARY_PLAYER;
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0; i < horizontalLines.length; i += 1) {
            LineState horizontalLine[] = horizontalLines[i];
            for (int j = 0; j < horizontalLine.length; j += 1) {
                switch (horizontalLine[j]) {
                    case SECONDARY_PLAYER:
                        horizontalLine[j] = LineState.PLAYER;
                        break;
                    case FREE:
                        break;
                    case PLAYER:
                        horizontalLine[j] = LineState.SECONDARY_PLAYER;
                        break;
                    default:
                        break;

                }
            }
        }
        for (int i = 0; i < verticalLines.length; i += 1) {
            LineState verticalLine[] = verticalLines[i];
            for (int j = 0; j < verticalLine.length; j += 1) {
                switch (verticalLine[j]) {
                    case SECONDARY_PLAYER:
                        verticalLine[j] = LineState.PLAYER;
                        break;
                    case FREE:
                        break;
                    case PLAYER:
                        verticalLine[j] = LineState.SECONDARY_PLAYER;
                        break;
                    default:
                        break;

                }
            }
        }

        switch (dotsGameSnapshot.getLastClickedLineState()) {
            case SECONDARY_PLAYER:
                dotsGameSnapshot.setLastClickedLineState(LineState.PLAYER);
                break;
            case FREE:
                break;
            case PLAYER:
                dotsGameSnapshot.setLastClickedLineState(LineState.SECONDARY_PLAYER);
                break;
            default:
                break;
        }

        List<Cell> scoredCells = dotsGameSnapshot.getLastScoredCells();
        for (Cell cell : scoredCells) {
            switch (cell.getCellState()) {
                case SECONDARY_PLAYER:
                    cell.setCellState(CellState.PLAYER);
                    break;
                case FREE:
                    break;
                case PLAYER:
                    cell.setCellState(CellState.SECONDARY_PLAYER);
                    break;
                default:
                    break;
            }
        }
    }
}
