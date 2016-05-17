package com.asb.goldtrap.models.sound.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.PropertiesDaoImpl;
import com.asb.goldtrap.models.sound.SoundModel;
import com.asb.goldtrap.models.sound.SoundType;

/**
 * Sound Model Impl.
 * Created by arjun on 17/05/16.
 */
public class SoundModelImpl implements SoundModel {

    private PropertiesDao propertiesDao;

    public SoundModelImpl(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        propertiesDao = new PropertiesDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public SoundType getSoundType() {
        return SoundType.valueOf(propertiesDao.getValue(PropertiesDao.SOUND_TYPE));
    }

    @Override
    public void updateSoundType(SoundType soundType) {
        propertiesDao.setValue(PropertiesDao.SOUND_TYPE, soundType.name());
    }
}
