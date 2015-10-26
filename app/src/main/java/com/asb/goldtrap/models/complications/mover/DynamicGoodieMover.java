package com.asb.goldtrap.models.complications.mover;

import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.states.enums.CellState;

import java.util.Set;

/**
 * Created by arjun on 26/10/15.
 */
public interface DynamicGoodieMover {
    void moveGoodie(CellState[][] cells, int cols, int rows, Set<DynamicGoodie> goodies,
                    DynamicGoodie goodie, int startRow, int startCol);
}
