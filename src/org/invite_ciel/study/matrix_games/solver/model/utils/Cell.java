package org.invite_ciel.study.matrix_games.solver.model.utils;

/**
 * Created by InviteCiel on 05.03.16.
 */
public class Cell implements Comparable<Cell>{
    private int row;
    private int column;
    private double value;

    public Cell(int row, int column, double value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    public Cell transposed() {
        return new Cell(column, row, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (row != cell.row) return false;
        if (column != cell.column) return false;
        return Double.compare(cell.value, value) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = row;
        result = 31 * result + column;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }


    @Override
    public int compareTo(Cell o) {
        return Double.compare(value, o.value);
    }
}
