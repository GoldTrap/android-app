package com.asb.goldtrap.models.conductor.factory.goodie.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.complications.goodies.impl.GoodiePositionModifier;
import com.asb.goldtrap.models.complications.goodies.impl.NullGoodieOperator;
import com.asb.goldtrap.models.complications.mover.impl.DiagonalMover;
import com.asb.goldtrap.models.complications.mover.impl.HorizontalMover;
import com.asb.goldtrap.models.complications.mover.impl.VerticalMover;
import com.asb.goldtrap.models.conductor.factory.goodie.GoodieOperatorFactory;
import com.asb.goldtrap.models.eo.Complication;

/**
 * Created by arjun on 31/10/15.
 */
public class PositionModifierGoodieOperator implements GoodieOperatorFactory {

    public static final String HORIZONTAL = "HORIZONTAL";
    public static final String VERTICAL = "VERTICAL";
    public static final String DIAGONAL = "DIAGONAL";

    @Override
    public GoodieOperator getGoodieOperator(Complication complication) {
        GoodieOperator goodieOperator;
        switch (complication.getStrategy().getType()) {
            case HORIZONTAL:
                goodieOperator = new GoodiePositionModifier(new HorizontalMover());
                break;
            case VERTICAL:
                goodieOperator = new GoodiePositionModifier(new VerticalMover());
                break;
            case DIAGONAL:
                goodieOperator = new GoodiePositionModifier(new DiagonalMover());
                break;
            default:
                goodieOperator = new NullGoodieOperator();
        }
        return goodieOperator;
    }
}
