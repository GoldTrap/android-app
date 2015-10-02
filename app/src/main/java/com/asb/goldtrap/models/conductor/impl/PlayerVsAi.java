package com.asb.goldtrap.models.conductor.impl;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.factory.DotsGameFactory;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;
import com.asb.goldtrap.models.solvers.impl.BasicGreedySolver;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.impl.AITurn;
import com.asb.goldtrap.models.states.impl.GameExited;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.models.states.impl.Gamer;
import com.asb.goldtrap.models.states.impl.PlayerTurn;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by arjun on 02/10/15.
 */
public class PlayerVsAi implements GameConductor {

    private static final String TAG = PlayerVsAi.class.getSimpleName();
    List<Line> combinations = new ArrayList<>();
    Set<Line> cSet = new HashSet<>();
    private AISolver aiSolver;
    private DotsGameSnapshot dotsGameSnapshot;
    private boolean extraChance;
    private GameState state;
    private GameState firstPlayerState;
    private GameState secondPlayerState;
    private GameState gameOverState;
    private GameState gameExitedState;
    private GameStateObserver mGameStateObserver;

    public PlayerVsAi(GameStateObserver gameStateObserver, int rows,
                      int cols, int goodiesCount) {
        dotsGameSnapshot = DotsGameFactory.createGameSnapshot(rows, cols, goodiesCount);
        firstPlayerState = new PlayerTurn(this, new Gamer());
        secondPlayerState = new AITurn(this, new Gamer());
        gameOverState = new GameOver(this, new Gamer());
        gameExitedState = new GameExited(this);
        state = firstPlayerState;
        mGameStateObserver = gameStateObserver;

        findAllLineCombinations();
        aiSolver = new BasicGreedySolver(dotsGameSnapshot, combinations);
    }

    private void findAllLineCombinations() {
        CellState[][] cells = dotsGameSnapshot.getCells();

        int horiRow = cells.length + 1;
        int horiCol = cells[0].length;

        for (int row = 0; row < horiRow; row += 1) {
            for (int col = 0; col < horiCol; col += 1) {
                Line line = new Line(LineType.HORIZONTAL, row, col);
                combinations.add(line);
                cSet.add(line);
            }
        }

        int vertiRow = cells.length;
        int vertiCol = cells[0].length + 1;

        for (int row = 0; row < vertiRow; row += 1) {
            for (int col = 0; col < vertiCol; col += 1) {
                Line line = new Line(LineType.VERTICAL, row, col);
                combinations.add(line);
                cSet.add(line);
            }
        }

    }

    @Override
    public void flipBoard() {
        state.flipBoard();
    }

    @Override
    public List<Line> getCombinations() {
        return combinations;
    }

    @Override
    public Set<Line> getcSet() {
        return cSet;
    }

    @Override
    public boolean playMyTurn() {
        return false;
    }

    @Override
    public boolean playMyTurn(LineType lineType, int row, int col) {
        boolean played = false;
        if (state instanceof PlayerTurn) {
            played = state.playTurn(lineType, row, col);
        }
        return played;
    }

    @Override
    public boolean playTheirTurn() {
        boolean played = false;
        if (state instanceof AITurn) {
            Line line = aiSolver.getNextLine();
            played = state.playTurn(line.lineType, line.row, line.col);
        }
        return played;
    }

    @Override
    public DotsGameSnapshot getGameSnapshot() {
        return dotsGameSnapshot;
    }

    @Override
    public void setState(GameState state) {
        this.state = state;
        this.mGameStateObserver.stateChanged(state);
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public GameState getFirstPlayerState() {
        return firstPlayerState;
    }

    @Override
    public GameState getSecondPlayerState() {
        return secondPlayerState;
    }

    @Override
    public GameState getOtherPlayerState() {
        GameState state = secondPlayerState;
        if (this.state == secondPlayerState) {
            state = firstPlayerState;
        }
        return state;
    }

    @Override
    public GameState getGameOverState() {
        return gameOverState;
    }

    @Override
    public GameState getGameExitedState() {
        return gameExitedState;
    }

    @Override
    public boolean isExtraChance() {
        return extraChance;
    }

    @Override
    public void setExtraChance(boolean extraChance) {
        this.extraChance = extraChance;
    }
}
