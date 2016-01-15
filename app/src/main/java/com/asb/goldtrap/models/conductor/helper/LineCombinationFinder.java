package com.asb.goldtrap.models.conductor.helper;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;

import java.util.List;
import java.util.Set;

/**
 * Created by arjun on 15/01/16.
 */
public interface LineCombinationFinder {

    void findAllLineCombinations(DotsGameSnapshot dotsGameSnapshot, List<Line> combinations,
                                 Set<Line> cSet);
}
