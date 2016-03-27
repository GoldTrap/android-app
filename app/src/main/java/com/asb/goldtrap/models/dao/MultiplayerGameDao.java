package com.asb.goldtrap.models.dao;

/**
 * MultiplayerGameDao.
 * Created by arjun on 27/03/16.
 */
public interface MultiplayerGameDao {

    String COMPLETED = "COMPLETED";
    String TABLE = "MULTIPLAYER_GAMES";
    String INDEX = "IDX_MULTIPLAYER_GAMES";
    String GAME = "GAME";
    String STATUS = "STATUS";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + GAME + " TEXT NOT NULL, "
            + STATUS + " TEXT NOT NULL"
            + ");";
    String INDEX_CREATE = "CREATE UNIQUE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + GAME + ", "
            + STATUS
            + ");";

    String getGameStatus(String game);

    long setGameStatus(String game, String status);

}
