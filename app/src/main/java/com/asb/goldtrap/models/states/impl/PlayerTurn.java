package com.asb.goldtrap.models.states.impl;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.helper.Gamer;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerTurn implements GameState {

    private GameConductor gameConductor;
    private Gamer gamer;
    private String myPlayerId;

    public PlayerTurn(GameConductor gameConductor, Gamer gamer, String myPlayerId) {
        this.gameConductor = gameConductor;
        this.gamer = gamer;
        this.myPlayerId = myPlayerId;
    }

    @Override
    public boolean playTurn(LineType lineType, int row, int col) {
        boolean played = false;
        Line line = new Line(lineType, row, col);
        if (gameConductor.isLineFree(line)) {
            for (Map.Entry<String, DotsGameSnapshot> snapshotEntry : gameConductor
                    .getGameSnapshotMap()
                    .entrySet()) {
                playTurn(lineType, row, col, snapshotEntry);
            }
            played = true;

            gameConductor.occupyLine(line);
            DotsGameSnapshot snapshot = gameConductor.getGameSnapshotMap().get(myPlayerId);
            if (!gamer.allCellsFilled(snapshot.getCells())) {
                if (snapshot.getLastScoredCells().isEmpty()) {
                    if (gameConductor.isExtraChance()) {
                        gameConductor.setExtraChance(false);
                    }
                    else {
                        gameConductor.setState(gameConductor.getOtherPlayerState());
                    }
                }
            }
            else {
                gameConductor.setState(gameConductor.getGameOverState());
            }
        }
        return played;
    }

    private void playTurn(LineType lineType, int row, int col,
                          Map.Entry<String, DotsGameSnapshot> snapshotEntry) {
        DotsGameSnapshot dotsGameSnapshot = snapshotEntry.getValue();
        LineState lineState = LineState.SECONDARY_PLAYER;
        CellState cellState = CellState.SECONDARY_PLAYER;
        if (snapshotEntry.getKey().equals(myPlayerId)) {
            lineState = LineState.PLAYER;
            cellState = CellState.PLAYER;
        }
        LineState[][] horizontalLines = dotsGameSnapshot.getHorizontalLines();
        LineState[][] verticalLines = dotsGameSnapshot.getVerticalLines();
        CellState[][] cells = dotsGameSnapshot.getCells();
        List<Cell> lastScoredCells;
        switch (lineType) {
            case HORIZONTAL:
                horizontalLines[row][col] = lineState;
                lastScoredCells = gamer.getBoundedCellsForHorizontal(cells,
                        horizontalLines, verticalLines, cellState, row, col);
                break;
            case VERTICAL:
                verticalLines[row][col] = lineState;
                lastScoredCells = gamer.getBoundedCellsForVertical(cells,
                        horizontalLines, verticalLines, cellState, row, col);
                break;
            case NONE:
            default:
                lastScoredCells = new ArrayList<>();
                break;

        }
        dotsGameSnapshot.setLastScoredCells(lastScoredCells);
        dotsGameSnapshot.setLastClickedLineState(lineState);
        dotsGameSnapshot.setLastClickedCol(col);
        dotsGameSnapshot.setLastClickedRow(row);
        dotsGameSnapshot.setLastClickedLineType(lineType);
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
        for (DotsGameSnapshot snapshot : gameConductor.getGameSnapshotMap().values()) {
            gamer.flipBoard(snapshot);
        }
        gameConductor.setState(gameConductor.getOtherPlayerState());
    }

}
