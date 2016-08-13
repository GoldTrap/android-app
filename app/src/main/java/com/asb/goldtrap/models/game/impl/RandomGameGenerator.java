package com.asb.goldtrap.models.game.impl;

import android.content.Context;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.game.GameGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Random Game Generator.
 * Created by arjun on 13/08/16.
 */
public class RandomGameGenerator implements GameGenerator {

    private Random random = new Random();

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateGame(Context context) {
        List<Episode> episodes = getEpisodes(context);
        Episode episode = episodes.get(random.nextInt(episodes.size()));
        Level level = episode.getLevel();
        return String.format(Locale.US, "%s%s%02d", episode.getCode(), level.getCode(),
                1 + random.nextInt((int) level.getNumberOfLevels()));
    }

    private List<Episode> getEpisodes(Context context) {
        return new Gson()
                .fromJson(new JsonReader(new InputStreamReader(
                                context.getResources().openRawResource(R.raw.episodes))),
                        new TypeToken<List<Episode>>() {
                        }.getType());
    }
}
