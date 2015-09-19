package com.asb.goldtrap.models.results;

public class Score {

    private String player;
    private int cellsOccupied;
    private int rowsOccupied;
    private int colsOccupied;
    private int oneKGoodies;

    public Score(String player, int cellsOccupied, int rowsOccupied,
                 int colsOccupied) {
        super();
        this.player = player;
        this.cellsOccupied = cellsOccupied;
        this.rowsOccupied = rowsOccupied;
        this.colsOccupied = colsOccupied;
    }

    public int getOneKGoodies() {
        return oneKGoodies;
    }

    public void setOneKGoodies(int oneKGoodies) {
        this.oneKGoodies = oneKGoodies;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getCellsOccupied() {
        return cellsOccupied;
    }

    public void setCellsOccupied(int cellsOccupied) {
        this.cellsOccupied = cellsOccupied;
    }

    public int getRowsOccupied() {
        return rowsOccupied;
    }

    public void setRowsOccupied(int rowsOccupied) {
        this.rowsOccupied = rowsOccupied;
    }

    public int getColsOccupied() {
        return colsOccupied;
    }

    public void setColsOccupied(int colsOccupied) {
        this.colsOccupied = colsOccupied;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player: ").append(player).append(", Cells: ")
                .append(cellsOccupied).append(", Rows: ").append(rowsOccupied)
                .append(", Cols: ").append(colsOccupied)
                .append(", One K Gold: ").append(oneKGoodies);
        return sb.toString();
    }
}
