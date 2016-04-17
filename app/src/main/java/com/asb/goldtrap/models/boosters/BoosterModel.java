package com.asb.goldtrap.models.boosters;

import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Map;

/**
 * BoosterModel.
 * Created by arjun on 09/04/16.
 */
public interface BoosterModel {
    Map<BoosterType, Booster> getBoostersState();

    Booster consumeBooster(GoogleApiClient client, BoosterType boosterType);

    Booster buyBooster(BoosterType boosterType, GoodiesState goodiesState);

    Booster buyBoosterWithPoints(BoosterType boosterType);
}
