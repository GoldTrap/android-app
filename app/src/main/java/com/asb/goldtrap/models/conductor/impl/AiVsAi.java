package com.asb.goldtrap.models.conductor.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.complications.goodies.impl.DynamicGoodieValueModifier;
import com.asb.goldtrap.models.complications.goodies.impl.NullGoodieMover;
import com.asb.goldtrap.models.complications.series.impl.AP;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.factory.DotsGameFactory;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;
import com.asb.goldtrap.models.solvers.factory.SolversFactory;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.models.states.impl.AITurn;
import com.asb.goldtrap.models.states.impl.GameExited;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.models.states.impl.Gamer;
import com.asb.goldtrap.models.states.impl.PlayerWhoIsAITurn;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AI vs AI game conductor
 */
public class AiVsAi implements GameConductor {

    private static final String TAG = AiVsAi.class.getSimpleName();
    List<Line> combinations = new ArrayList<>();
    Set<Line> cSet = new HashSet<>();
    private AISolver aiSolver;
    private AISolver otherAiSolver;
    private DotsGameSnapshot dotsGameSnapshot;
    private boolean extraChance;
    private GameState state;
    private GameState firstPlayerState;
    private GameState secondPlayerState;
    private GameState gameOverState;
    private GameState gameExitedState;
    private GameStateObserver mGameStateObserver;
    private List<GoodieOperator> goodieOperators;

    public AiVsAi(SolversFactory solversFactory, GameStateObserver gameStateObserver, int rows,
                  int cols, int goodiesCount, int blockedCount) {
        dotsGameSnapshot =
                DotsGameFactory.createGameSnapshot(rows, cols, goodiesCount, blockedCount);
        firstPlayerState = new PlayerWhoIsAITurn(this, new Gamer());
        secondPlayerState = new AITurn(this, new Gamer());
        gameOverState = new GameOver(this, new Gamer());
        gameExitedState = new GameExited(this);
        state = firstPlayerState;
        mGameStateObserver = gameStateObserver;
        goodieOperators = new ArrayList<>();
        goodieOperators.add(new DynamicGoodieValueModifier(new AP(100)));
        findAllLineCombinations();
        aiSolver = solversFactory.getPlayerSolver(dotsGameSnapshot, combinations);
        otherAiSolver = solversFactory.getOtherPlayerSolver(dotsGameSnapshot, combinations);
    }

    public void flipBoard() {
        state.flipBoard();
    }

    private void findAllLineCombinations() {
        CellState[][] cells = dotsGameSnapshot.getCells();
        LineState[][] horizontalLines = dotsGameSnapshot.getHorizontalLines();

        int horiRow = cells.length + 1;
        int horiCol = cells[0].length;

        for (int row = 0; row < horiRow; row += 1) {
            for (int col = 0; col < horiCol; col += 1) {
                if (horizontalLines[row][col] == LineState.FREE) {
                    Line line = new Line(LineType.HORIZONTAL, row, col);
                    combinations.add(line);
                    cSet.add(line);
                }
            }
        }

        int vertiRow = cells.length;
        int vertiCol = cells[0].length + 1;
        LineState[][] verticalLines = dotsGameSnapshot.getVerticalLines();
        for (int row = 0; row < vertiRow; row += 1) {
            for (int col = 0; col < vertiCol; col += 1) {
                if (verticalLines[row][col] == LineState.FREE) {
                    Line line = new Line(LineType.VERTICAL, row, col);
                    combinations.add(line);
                    cSet.add(line);
                }
            }
        }

    }

    public List<Line> getCombinations() {
        return combinations;
    }

    public Set<Line> getcSet() {
        return cSet;
    }

    public boolean playMyTurn() {
        boolean played = false;
        if (state instanceof AITurn && state == firstPlayerState) {
            Line line = aiSolver.getNextLine();
            played = state.playTurn(line.lineType, line.row, line.col);
        }
        return played;
    }

    @Override
    public boolean playMyTurn(LineType lineType, int row, int col) {
        return false;
    }

    public boolean playTheirTurn() {
        boolean played = false;
        if (state instanceof AITurn && state == secondPlayerState) {
            Line line = otherAiSolver.getNextLine();
            played = state.playTurn(line.lineType, line.row, line.col);
        }
        return played;
    }

    public DotsGameSnapshot getGameSnapshot() {
        return dotsGameSnapshot;
    }

    private int getCells() {
        CellState[][] cells = dotsGameSnapshot.getCells();
        int rows = cells.length;
        int cols = cells[0].length;
        return rows * cols;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
        if (state instanceof GameOver) {
            this.dotsGameSnapshot.computeScore();
        }
        this.mGameStateObserver.stateChanged(state);
    }

    public GameState getOtherPlayerState() {
        GameState state = secondPlayerState;
        if (this.state == secondPlayerState) {
            state = firstPlayerState;
        }
        return state;
    }

    public GameState getFirstPlayerState() {
        return firstPlayerState;
    }

    public GameState getSecondPlayerState() {
        return secondPlayerState;
    }

    public GameState getGameOverState() {
        return gameOverState;
    }

    public GameState getGameExitedState() {
        return gameExitedState;
    }

    public boolean isExtraChance() {
        return extraChance;
    }

    public void setExtraChance(boolean extraChance) {
        this.extraChance = extraChance;
    }

    @Override
    public void doPostProcess() {
        for (GoodieOperator goodieOperator : goodieOperators) {
            goodieOperator.operateOnGoodie(dotsGameSnapshot);
        }
    }
}
