package com.asb.goldtrap.models.complications.mover;

import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.states.enums.CellState;

import java.util.Set;

/**
 * Created by arjun on 25/10/15.
 */
public interface GoodieMover {
    void moveGoodie(CellState[][] cells, int cols, int rows, Set<Goodie> goodies,
                    Goodie goodie, int startRow, int startCol);
}
