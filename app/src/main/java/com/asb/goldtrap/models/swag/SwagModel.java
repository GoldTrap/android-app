package com.asb.goldtrap.models.swag;

import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Swag Model.
 * Created by arjun on 18/07/16.
 */
public interface SwagModel {

    void addGoodieCount(GoodiesState goodiesState, int count);

}
