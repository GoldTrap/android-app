package com.asb.goldtrap.models.states;

import com.asb.goldtrap.views.LineType;

public interface GameState {
    /**
     * Play your turn
     *
     * @param lineType lineType
     * @param row      row of the line
     * @param col      column of the line
     */
    boolean playTurn(LineType lineType, int row, int col);

    /**
     * Exit the game
     */
    void exitGame();

    /**
     * Game over, no more turns to play
     */
    void gameOver();

    /**
     * Get all cells and lines owned by your opponent
     */
    void flipBoard();
}
