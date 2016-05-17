package com.asb.goldtrap.models.sound.strategy.factory;

import android.content.Context;

import com.asb.goldtrap.models.sound.SoundType;
import com.asb.goldtrap.models.sound.strategy.SoundHelper;
import com.asb.goldtrap.models.sound.strategy.impl.GuitarSoundHelper;
import com.asb.goldtrap.models.sound.strategy.impl.MuteSoundHelper;

/**
 * SoundFactory.
 * Created by arjun on 17/05/16.
 */
public class SoundFactory {

    public SoundHelper getSoundHelper(SoundType soundType, Context context) {
        SoundHelper soundHelper = null;
        switch (soundType) {
            case MUTE:
                soundHelper = new MuteSoundHelper();
                break;
            case GUITAR:
                soundHelper = GuitarSoundHelper.instance(context);
                break;
        }
        return soundHelper;
    }

}
