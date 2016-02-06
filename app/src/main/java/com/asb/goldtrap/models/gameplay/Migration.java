package com.asb.goldtrap.models.gameplay;

/**
 * Created by arjun on 04/10/15.
 */
public interface Migration {
    String MIGRATION_COMPLETE = "MIGRATION_COMPLETE";
    String FILE_MIGRATION_COMPLETE = "FILE_MIGRATION_COMPLETE";
    String DATA_MIGRATION_COMPLETE = "DATA_MIGRATION_COMPLETE";

    boolean isMigrationComplete();

    void doMigrationOfData();
}
