package com.asb.goldtrap.models.conductor;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.GameState;

import java.util.List;
import java.util.Set;

/**
 * Created by arjun on 19/09/15.
 */
public interface GameConductor {

    interface GameStateObserver {
        void stateChanged(GameState state);
    }

    void flipBoard();

    List<Line> getCombinations();

    Set<Line> getcSet();

    boolean playFirstPlayerTurn();

    boolean playSecondPlayerTurn();

    DotsGameSnapshot getGameSnapshot();

    void setState(GameState state);

    GameState getState();

    GameState getFirstPlayerState();

    GameState getSecondPlayerState();

    GameState getOtherPlayerState();

    GameState getGameOverState();

    GameState getGameExitedState();

    boolean isExtraChance();

    void setExtraChance(boolean extraChance);
}
