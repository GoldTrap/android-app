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
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.snapshots.GameAndLevelSnapshot;
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
import java.util.Set;

/**
 * Created by arjun on 15/01/16.
 */
public class PlayerVsPlayer implements GameConductor {

    private static final String TAG = PlayerVsAi.class.getSimpleName();
    private List<Line> combinations = new ArrayList<>();
    private Set<Line> cSet = new HashSet<>();
    private LineCombinationFinder lineCombinationFinder = new LineCombinationFinderImpl();
    private Map<String, DotsGameSnapshot> snapshotMap = new HashMap<>();
    private boolean extraChance;
    private GameState state;
    private GameState firstPlayerState;
    private GameState secondPlayerState;
    private GameState gameOverState;
    private GameState gameExitedState;
    private GameStateObserver mGameStateObserver;
    private List<GoodieOperator> goodieOperators;
    private String myPlayerId;
    private GoodieOperatorFactory goodieOperatorFactory = new GenericGoodieOperator();
    private final Gamer gamer;

    public PlayerVsPlayer(GameStateObserver gameStateObserver,
                          GameAndLevelSnapshot gameAndLevelSnapshot, String myPlayerId) {
        this.myPlayerId = myPlayerId;
        init(gameAndLevelSnapshot);
        gamer = new GamerImpl();
        Level level = gameAndLevelSnapshot.getLevel();
        firstPlayerState = new PlayerTurn(this, gamer, myPlayerId);
        secondPlayerState =
                new SecondaryPlayerTurn(this, gamer, getSecondPlayerId(gameAndLevelSnapshot));
        gameOverState = new GameOver(this, gamer);
        gameExitedState = new GameExited(this);
        mGameStateObserver = gameStateObserver;
        goodieOperators = new ArrayList<>();
        addOperators(level);
    }

    private String getSecondPlayerId(GameAndLevelSnapshot gameAndLevelSnapshot) {
        for (String playerId : gameAndLevelSnapshot.getSnapshotMap().keySet()) {
            if (!myPlayerId.equals(playerId)) {
                return playerId;
            }
        }
        return null;
    }

    public void init(GameAndLevelSnapshot gameAndLevelSnapshot) {
        snapshotMap = gameAndLevelSnapshot.getSnapshotMap();
        lineCombinationFinder.findAllLineCombinations(snapshotMap.get(myPlayerId), combinations,
                cSet);
    }

    public void reInit(GameAndLevelSnapshot gameAndLevelSnapshot) {
        init(gameAndLevelSnapshot);
        if (isGameOver()) {
            state = gameOverState;
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
        return false;
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
        for (DotsGameSnapshot snapshot : snapshotMap.values()) {
            for (GoodieOperator goodieOperator : goodieOperators) {
                goodieOperator.operateOnGoodie(snapshot);
            }
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
        return gamer.allCellsFilled(snapshotMap.get(myPlayerId).getCells());
    }
}
