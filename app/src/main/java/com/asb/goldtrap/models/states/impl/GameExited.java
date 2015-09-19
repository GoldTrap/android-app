package com.asb.goldtrap.models.states.impl;

import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.views.LineType;

public class GameExited implements GameState {

    private GameConductor gameConductor;

    public GameExited(GameConductor gameConductor) {
        this.gameConductor = gameConductor;
    }

    @Override
    public boolean playTurn(LineType lineType, int row, int col) {
        return false;
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
    }

}
