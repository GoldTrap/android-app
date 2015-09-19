package com.asb.goldtrap.models.states.impl;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

import java.util.List;
import java.util.Set;

public class AITurn implements GameState {

    private GameConductor gameConductor;
    private Gamer gamer;

    public AITurn(GameConductor gameConductor, Gamer gamer) {
        this.gameConductor = gameConductor;
        this.gamer = gamer;
    }

    @Override
    public boolean playTurn(LineType lineType, int row, int col) {
        boolean played = true;
        DotsGameSnapshot dotsGameSnapshot = gameConductor.getGameSnapshot();
        Set<Line> cSet = gameConductor.getcSet();
        List<Line> combinations = gameConductor.getCombinations();
        Line line = new Line(lineType, row, col);

        combinations.remove(line);
        cSet.remove(line);
        LineState lineState = getLineState();
        CellState cellState = getCellState();
        LineState[][] horizontalLines = dotsGameSnapshot.getHorizontalLines();
        LineState[][] verticalLines = dotsGameSnapshot.getVerticalLines();
        CellState[][] cells = dotsGameSnapshot.getCells();
        List<Cell> lastScoredCells;
        if (LineType.HORIZONTAL == line.lineType) {
            horizontalLines[row][col] = lineState;
            lastScoredCells = gamer.getBoundedCellsForHorizontal(cells,
                    horizontalLines, verticalLines, cellState, row, col);
        }
        else {
            verticalLines[row][col] = lineState;
            lastScoredCells = gamer.getBoundedCellsForVertical(cells,
                    horizontalLines, verticalLines, cellState, row, col);
        }
        dotsGameSnapshot.setLastScoredCells(lastScoredCells);
        dotsGameSnapshot.setLastClickedLineState(lineState);
        dotsGameSnapshot.setLastClickedCol(col);
        dotsGameSnapshot.setLastClickedRow(row);
        dotsGameSnapshot.setLastClickedLineType(lineType);
        // dotBoard.setGameSnapShot(dotsGameSnapshot);

        if (!gamer.allCellsFilled(cells)) {
            if (lastScoredCells.isEmpty()) {
                gameConductor.setState(gameConductor.getOtherPlayerState());
            }
        }
        else {
            gameConductor.setState(gameConductor.getGameOverState());
        }
        return played;
    }

    protected CellState getCellState() {
        return CellState.AI;
    }

    protected LineState getLineState() {
        return LineState.AI;
    }

    @Override
    public void exitGame() {
        // TODO Auto-generated method stub

    }

    @Override
    public void gameOver() {
        // TODO Auto-generated method stub

    }

    @Override
    public void flipBoard() {
        gamer.flipBoard(gameConductor.getGameSnapshot());
        gameConductor.setState(gameConductor.getOtherPlayerState());
    }

}
