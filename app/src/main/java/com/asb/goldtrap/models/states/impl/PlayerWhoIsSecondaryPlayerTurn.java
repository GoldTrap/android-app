package com.asb.goldtrap.models.states.impl;

import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.conductor.helper.Gamer;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;

/**
 * Created by arjun on 19/09/15.
 */
public class PlayerWhoIsSecondaryPlayerTurn extends SecondaryPlayerTurn {
    public PlayerWhoIsSecondaryPlayerTurn(GameConductor gameConductor,
                                          Gamer gamer, String playerId) {
        super(gameConductor, gamer, playerId);
    }

    protected CellState getCellState() {
        return CellState.PLAYER;
    }

    protected LineState getLineState() {
        return LineState.PLAYER;
    }
}
