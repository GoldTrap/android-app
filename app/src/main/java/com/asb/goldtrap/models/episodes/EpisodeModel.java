package com.asb.goldtrap.models.episodes;

import com.asb.goldtrap.models.eo.Episode;

/**
 * Created by arjun on 06/02/16.
 */
public interface EpisodeModel {

    interface Listener {
        void dataChanged();
    }

    void loadEpisodes();

    Episode getEpisode(int position);

    int getEpisodeCount();
}
