package com.asb.goldtrap.models.states.impl;

import com.asb.goldtrap.models.conductor.GameConductor;
import com.asb.goldtrap.models.states.GameState;
import com.asb.goldtrap.views.LineType;

public class GameOver implements GameState {

    private GameConductor gameConductor;

    private Gamer gamer;

    public GameOver(GameConductor gameConductor, Gamer gamer) {
        this.gameConductor = gameConductor;
        this.gamer = gamer;
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
        gamer.flipBoard(gameConductor.getGameSnapshot());
    }

}
