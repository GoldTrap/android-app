package com.asb.goldtrap.models.components;

import com.asb.goldtrap.views.LineType;

public class Line {

    public LineType lineType;
    public int row;
    public int col;

    public Line(LineType lineType, int row, int col) {
        super();
        this.lineType = lineType;
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Line) {
            Line line = (Line) o;
            if (line.lineType == this.lineType && line.row == this.row
                    && line.col == this.col) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.lineType.toString().hashCode() + row + col;
    }
}
