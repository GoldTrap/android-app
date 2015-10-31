package com.asb.goldtrap.models.conductor.factory.goodie;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.eo.Complication;

/**
 * Created by arjun on 31/10/15.
 */
public interface GoodieOperatorFactory {
    GoodieOperator getGoodieOperator(Complication complication);
}
