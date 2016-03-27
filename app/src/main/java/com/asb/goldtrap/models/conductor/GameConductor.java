package com.asb.goldtrap.models.conductor;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.views.LineType;

import java.util.Map;

/**
 * Created by arjun on 19/09/15.
 */
public interface GameConductor {

    boolean isLineFree(Line line);

    void occupyLine(Line line);

    interface GameStateObserver {
        void stateChanged(GameState state);
    }

    void flipBoard();

    void skipTurn();

    boolean playMyTurn();

    boolean playMyTurn(LineType lineType, int row, int col);

    boolean playTheirTurn();

    void doPostProcess();

    Map<String, DotsGameSnapshot> getGameSnapshotMap();

    void setState(GameState state);

    GameState getState();

    GameState getFirstPlayerState();

    GameState getSecondPlayerState();

    GameState getOtherPlayerState();

    GameState getGameOverState();

    GameState getGameExitedState();

    boolean isExtraChance();

    void setExtraChance(boolean extraChance);

    boolean isGameOver();
}
