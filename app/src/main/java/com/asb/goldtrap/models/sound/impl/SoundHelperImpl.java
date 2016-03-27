package com.asb.goldtrap.models.sound.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.sound.SoundHelper;
import com.asb.goldtrap.models.sound.SoundType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * SoundHelperImpl.
 * Created by arjun on 27/03/16.
 */
public class SoundHelperImpl implements SoundHelper {
    private static final List<Integer> SOUND_POOL = Arrays
            .asList(R.raw.click_1, R.raw.click_2, R.raw.click_3, R.raw.cell_1, R.raw.win_1,
                    R.raw.lose_1);
    private static final List<Integer> CLICK_POOL = Arrays
            .asList(R.raw.click_1, R.raw.click_2, R.raw.click_3);
    private static final List<Integer> CELL_POOL = Arrays
            .asList(R.raw.cell_1);
    private static final List<Integer> WIN_POOL = Arrays
            .asList(R.raw.win_1);
    private static final List<Integer> LOSE_POOL = Arrays
            .asList(R.raw.lose_1);
    private static final List<Integer> DRAW_POOL = Arrays
            .asList(R.raw.cell_1);
    private Map<Integer, Integer> poolIdMap = new HashMap<>();
    private final Random random = new Random();

    private final SoundPool soundPool;

    private static SoundHelper INSTANCE;

    public static SoundHelper instance(Context context) {
        if (null == INSTANCE) {
            synchronized (SoundHelperImpl.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SoundHelperImpl(context);
                }
            }
        }
        return INSTANCE;
    }

    private SoundHelperImpl(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = createNewSoundPool();
        }
        else {
            soundPool = createOldSoundPool();
        }
        for (Integer resourceId : SOUND_POOL) {
            poolIdMap.put(resourceId, soundPool.load(context, resourceId, 1));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        return new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    private SoundPool createOldSoundPool() {
        return new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public void playSound(DotsGameSnapshot snapshot) {
        this.playSound(getSoundType(snapshot));
    }

    @Override
    public void playSound(SoundType soundType) {
        int resource = getResource(soundType);
        soundPool.play(poolIdMap.get(resource), 1, 1, 1, 0, 1);
    }

    private int getResource(SoundType soundType) {
        int resource = -1;
        switch (soundType) {
            case CLICK:
                resource = CLICK_POOL.get(random.nextInt(CLICK_POOL.size()));
                break;
            case CELL:
                resource = CELL_POOL.get(0);
                break;
            case WIN:
                resource = WIN_POOL.get(0);
                break;
            case LOSE:
                resource = LOSE_POOL.get(0);
                break;
            case DRAW:
                resource = DRAW_POOL.get(0);
                break;
        }
        return resource;
    }

    private SoundType getSoundType(DotsGameSnapshot snapshot) {
        SoundType soundType;
        if (!snapshot.getLastScoredCells().isEmpty()) {
            soundType = SoundType.CELL;
        }
        else {
            soundType = SoundType.CLICK;
        }
        return soundType;
    }
}
