package com.asb.goldtrap.models.conductor.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.factory.goodie.GoodieOperatorFactory;
import com.asb.goldtrap.models.conductor.factory.goodie.impl.GenericGoodieOperator;
import com.asb.goldtrap.models.conductor.helper.Gamer;
import com.asb.goldtrap.models.conductor.helper.LineCombinationFinder;
import com.asb.goldtrap.models.conductor.helper.impl.GamerImpl;
import com.asb.goldtrap.models.conductor.helper.impl.LineCombinationFinderImpl;
import com.asb.goldtrap.models.eo.Complication;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.factory.GameSnapshotCreator;
import com.asb.goldtrap.models.results.computers.result.ScoreComputer;
import com.asb.goldtrap.models.results.computers.result.impl.ScoreComputerImpl;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;
import com.asb.goldtrap.models.solvers.factory.SolversFactory;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.impl.GameExited;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.models.states.impl.PlayerWhoIsSecondaryPlayerTurn;
import com.asb.goldtrap.models.states.impl.SecondaryPlayerTurn;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AI vs AI game conductor
 */
public class AiVsAi implements GameConductor {

    private static final String TAG = AiVsAi.class.getSimpleName();
    public static final String DEFAULT = "DEFAULT";
    List<Line> combinations = new ArrayList<>();
    Set<Line> cSet = new HashSet<>();
    private GameSnapshotCreator gameSnapshotCreator = new GameSnapshotCreator();
    private LineCombinationFinder lineCombinationFinder = new LineCombinationFinderImpl();
    private AISolver aiSolver;
    private AISolver otherAiSolver;
    private Map<String, DotsGameSnapshot> snapshotMap = new HashMap<>();
    private boolean extraChance;
    private GameState state;
    private GameState firstPlayerState;
    private GameState secondPlayerState;
    private GameState gameOverState;
    private GameState gameExitedState;
    private GameStateObserver mGameStateObserver;
    private List<GoodieOperator> goodieOperators;
    private ScoreComputer scoreComputer;
    private GoodieOperatorFactory goodieOperatorFactory = new GenericGoodieOperator();
    private final Gamer gamer;

    public AiVsAi(SolversFactory solversFactory, GameStateObserver gameStateObserver, Level level) {
        gamer = new GamerImpl();
        snapshotMap.put(DEFAULT, gameSnapshotCreator.createGameSnapshot(level));
        firstPlayerState = new PlayerWhoIsSecondaryPlayerTurn(this, gamer, DEFAULT);
        secondPlayerState = new SecondaryPlayerTurn(this, gamer, DEFAULT);
        gameOverState = new GameOver(this, gamer);
        gameExitedState = new GameExited(this);
        state = firstPlayerState;
        mGameStateObserver = gameStateObserver;
        goodieOperators = new ArrayList<>();
        addOperators(level);
        lineCombinationFinder.findAllLineCombinations(snapshotMap.get(DEFAULT), combinations, cSet);
        aiSolver = solversFactory.getPlayerSolver(snapshotMap.get(DEFAULT), combinations);
        otherAiSolver = solversFactory.getOtherPlayerSolver(snapshotMap.get(DEFAULT), combinations);
        scoreComputer = new ScoreComputerImpl(snapshotMap.get(DEFAULT));
    }

    private void addOperators(Level level) {
        for (Complication complication : level.getComplications()) {
            GoodieOperator operator = goodieOperatorFactory.getGoodieOperator(complication);
            goodieOperators.add(operator);
        }
    }

    public void flipBoard() {
        state.flipBoard();
    }

    @Override
    public void skipTurn() {
        this.setState(this.getOtherPlayerState());
    }

    public boolean playMyTurn() {
        boolean played = false;
        if (state instanceof SecondaryPlayerTurn && state == firstPlayerState) {
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
        if (state instanceof SecondaryPlayerTurn && state == secondPlayerState) {
            Line line = otherAiSolver.getNextLine();
            played = state.playTurn(line.lineType, line.row, line.col);
        }
        return played;
    }

    public Map<String, DotsGameSnapshot> getGameSnapshotMap() {
        return snapshotMap;
    }

    private int getCells() {
        CellState[][] cells = snapshotMap.get(DEFAULT).getCells();
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
            scoreComputer.computeScoreWithResults();
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
            goodieOperator.operateOnGoodie(snapshotMap.get(DEFAULT));
        }
    }

    @Override
    public boolean isLineFree(Line line) {
        return cSet.contains(line);
    }

    @Override
    public void occupyLine(Line line) {
        combinations.remove(line);
        cSet.remove(line);
    }


    @Override
    public boolean isGameOver() {
        return gamer.allCellsFilled(snapshotMap.get(DEFAULT).getCells());
    }
}
