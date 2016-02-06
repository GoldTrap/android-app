package com.asb.goldtrap.models.episodes;

import com.asb.goldtrap.models.eo.Episode;

/**
 * Created by arjun on 06/02/16.
 */
public interface EpisodeModel {
    void loadAllEpisodes();

    Episode getEpisode(int position);

    int getEpisodeCount();

    void close();
}
