package com.asb.goldtrap.models.boosters;

import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.Map;

/**
 * BoosterModel.
 * Created by arjun on 09/04/16.
 */
public interface BoosterModel {
    Map<BoosterType, Booster> getBoostersState();

    Booster consumeBooster(BoosterType boosterType);

    Booster buyBooster(BoosterType boosterType, GoodiesState goodiesState);
}
