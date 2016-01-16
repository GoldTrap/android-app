package com.asb.goldtrap.models.snapshots;

import com.asb.goldtrap.models.eo.Level;

import java.util.Map;

/**
 * Created by arjun on 15/01/16.
 */
public class GameAndLevelSnapshot {
    private String lastPlayerId;
    private Map<String, DotsGameSnapshot> snapshotMap;
    private Level level;

    public GameAndLevelSnapshot() {
    }

    public GameAndLevelSnapshot(Map<String, DotsGameSnapshot> snapshotMap,
                                Level level) {
        this.snapshotMap = snapshotMap;
        this.level = level;
    }

    public String getLastPlayerId() {
        return lastPlayerId;
    }

    public void setLastPlayerId(String lastPlayerId) {
        this.lastPlayerId = lastPlayerId;
    }

    public Map<String, DotsGameSnapshot> getSnapshotMap() {
        return snapshotMap;
    }

    public void setSnapshotMap(
            Map<String, DotsGameSnapshot> snapshotMap) {
        this.snapshotMap = snapshotMap;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
