package com.asb.goldtrap.models.sound;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;

/**
 * SoundHelper.
 * Created by arjun on 27/03/16.
 */
public interface SoundHelper {
    void playSound(DotsGameSnapshot snapshot);

    void playSound(SoundType soundType);
}
