package com.asb.goldtrap.models.game;

import android.content.Context;

/**
 * Game Generator.
 * Created by arjun on 13/08/16.
 */
public interface GameGenerator {
    /**
     * Generate the game and give the resource.
     *
     * @param context context
     * @return game episode
     */
    String generateGame(Context context);

}
