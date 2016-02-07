package com.asb.goldtrap.models.levels;

import com.asb.goldtrap.models.eo.migration.Level;

/**
 * Created by arjun on 07/02/16.
 */
public interface LevelModel {
    interface Listener {
        void dataChanged();
    }

    void loadLevels(String episodeCode);

    Level getLevel(int position);

    int getLevelCount();
}
