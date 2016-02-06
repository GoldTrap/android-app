package com.asb.goldtrap.models.gameplay.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.dao.EpisodeDao;
import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.impl.EpisodeDaoImpl;
import com.asb.goldtrap.models.dao.impl.LevelDaoImpl;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.gameplay.Migration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by arjun on 06/02/16.
 */
public class FileToDBMigrationImpl implements Migration {
    private String TAG = FileToDBMigrationImpl.class.getSimpleName();
    private List<Episode> episodes;
    private PropertiesDao propertiesDao;
    private EpisodeDao episodeDao;
    private LevelDao levelDao;

    public FileToDBMigrationImpl(Context context, SQLiteOpenHelper dbHelper,
                                 PropertiesDao propertiesDao) {
        this.propertiesDao = propertiesDao;
        episodeDao = new EpisodeDaoImpl(dbHelper.getWritableDatabase());
        levelDao = new LevelDaoImpl(dbHelper.getWritableDatabase());
        this.episodes = getEpisodes(context, R.raw.episodes);
    }

    private List<Episode> getEpisodes(Context context, int resourceId) {
        return new Gson()
                .fromJson(new JsonReader(new InputStreamReader(
                                context.getResources().openRawResource(resourceId))),
                        new TypeToken<List<Episode>>() {
                        }.getType());
    }

    @Override
    public boolean isMigrationComplete() {
        return !TextUtils.isEmpty(propertiesDao.getValue(FILE_MIGRATION_COMPLETE));
    }

    @Override
    public void doMigrationOfData() {
        for (Episode episode : this.episodes) {
            long episodeId = episodeDao.save(episode);
            Log.d(TAG, "Episode " + episode.getCode() + " added");
            Level level = episode.getLevel();
            for (long levelNumber = 1; levelNumber <= level.getNumberOfLevels(); levelNumber += 1) {
                levelDao.save(episodeId, episode.getCode(), levelNumber, level);
                Log.d(TAG, "Level " + levelNumber + " added");
            }
        }
        propertiesDao.setValue(FILE_MIGRATION_COMPLETE, "YES");
    }
}
