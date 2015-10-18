package com.asb.goldtrap.models.dao;

/**
 * Created by arjun on 18/10/15.
 */
public interface ChapterDao {
    String TABLE = "CHAPTER";
    String ID = "_id";
    String NUMBER = "NUMBER";
    String CODE = "CODE";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NUMBER + " INT NOT NULL, "
            + CODE + " TEXT NOT NULL"
            + ");";
}
