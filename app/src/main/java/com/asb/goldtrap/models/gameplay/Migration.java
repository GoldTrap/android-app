package com.asb.goldtrap.models.gameplay;

/**
 * Created by arjun on 04/10/15.
 */
public interface Migration {
    interface Listener {
        void migrationComplete();
    }

    void doMigrationOfData();
}
