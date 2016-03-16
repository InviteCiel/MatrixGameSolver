package org.invite_ciel.study.matrix_games.solver.model.utils;

/**
 * Created by InviteCiel on 05.03.16.
 */
public class CellIndex {
    private final int row;
    private final int column;

    public CellIndex(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
