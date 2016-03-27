package com.asb.goldtrap.models.conductor.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.factory.goodie.GoodieOperatorFactory;
import com.asb.goldtrap.models.conductor.factory.goodie.impl.GenericGoodieOperator;
import com.asb.goldtrap.models.conductor.factory.solver.SolverFactory;
import com.asb.goldtrap.models.conductor.factory.solver.impl.SolverFactoryImpl;
import com.asb.goldtrap.models.conductor.helper.Gamer;
import com.asb.goldtrap.models.conductor.helper.LineCombinationFinder;
import com.asb.goldtrap.models.conductor.helper.impl.GamerImpl;
import com.asb.goldtrap.models.conductor.helper.impl.LineCombinationFinderImpl;
import com.asb.goldtrap.models.eo.Complication;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.factory.GameSnapshotCreator;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.models.states.impl.GameExited;
import com.asb.goldtrap.models.states.impl.GameOver;
import com.asb.goldtrap.models.states.impl.PlayerTurn;
import com.asb.goldtrap.models.states.impl.SecondaryPlayerTurn;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by arjun on 02/10/15.
 */
public class PlayerVsAi implements GameConductor {

    private static final String TAG = PlayerVsAi.class.getSimpleName();
    public static final String DEFAULT = "DEFAULT";
    private List<Line> combinations = new ArrayList<>();
    private Set<Line> cSet = new HashSet<>();
    private Random random = new Random();
    private GameSnapshotCreator gameSnapshotCreator = new GameSnapshotCreator();
    private LineCombinationFinder lineCombinationFinder = new LineCombinationFinderImpl();
    private SolverFactory solverFactory = new SolverFactoryImpl();
    private AISolver aiSolver;
    private Map<String, DotsGameSnapshot> snapshotMap = new HashMap<>();
    private boolean extraChance;
    private GameState state;
    private GameState firstPlayerState;
    private GameState secondPlayerState;
    private GameState gameOverState;
    private GameState gameExitedState;
    private GameStateObserver mGameStateObserver;
    private List<GoodieOperator> goodieOperators;
    private GoodieOperatorFactory goodieOperatorFactory = new GenericGoodieOperator();
    private final Gamer gamer;

    public PlayerVsAi(GameStateObserver gameStateObserver, Level level) {
        gamer = new GamerImpl();
        snapshotMap.put(DEFAULT, gameSnapshotCreator.createGameSnapshot(level));
        firstPlayerState = new PlayerTurn(this, gamer, DEFAULT);
        secondPlayerState = new SecondaryPlayerTurn(this, gamer, DEFAULT);
        gameOverState = new GameOver(this, gamer);
        gameExitedState = new GameExited(this);
        mGameStateObserver = gameStateObserver;
        goodieOperators = new ArrayList<>();
        addOperators(level);
        lineCombinationFinder.findAllLineCombinations(snapshotMap.get(DEFAULT), combinations,
                cSet);
        aiSolver = solverFactory.getAISolver(level, snapshotMap.get(DEFAULT), combinations);
        setGameState(level);
    }


    private void setGameState(Level level) {
        switch (level.getFirstPlayer()) {
            case ANY:
                if (random.nextBoolean()) {
                    state = firstPlayerState;
                }
                else {
                    state = secondPlayerState;
                }
                break;
            case ME:
                state = firstPlayerState;
                break;
            case THEY:
                state = secondPlayerState;
                break;
            default:
                state = firstPlayerState;
        }
        if (state == secondPlayerState) {
            playTheirTurn();
        }
    }

    private void addOperators(Level level) {
        for (Complication complication : level.getComplications()) {
            GoodieOperator operator = goodieOperatorFactory.getGoodieOperator(complication);
            goodieOperators.add(operator);
        }
    }

    @Override
    public void flipBoard() {
        state.flipBoard();
    }

    @Override
    public void skipTurn() {
        this.setState(this.getOtherPlayerState());
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
        if (state instanceof SecondaryPlayerTurn) {
            Line line = aiSolver.getNextLine();
            played = state.playTurn(line.lineType, line.row, line.col);
        }
        return played;
    }

    @Override
    public Map<String, DotsGameSnapshot> getGameSnapshotMap() {
        return snapshotMap;
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
