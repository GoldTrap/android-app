package com.asb.goldtrap.models.conductor;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.views.LineType;

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

    boolean playMyTurn();

    boolean playMyTurn(LineType lineType, int row, int col);

    boolean playTheirTurn();

    void doPostProcess();

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
