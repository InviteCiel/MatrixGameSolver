package org.invite_ciel.study.matrix_games.solver.model.utils;

import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;

import java.util.Comparator;

/**
 * Created by InviteCiel on 05.03.16.
 */
public class CellIndexComparator implements Comparator<Cell> {
    private final MutableRealMatrix model;

    public CellIndexComparator(MutableRealMatrix model) {
        this.model = model;
    }

    @Override
    public int compare(Cell cell1, Cell cell2) {
        return Double.compare(model.getData()[cell1.getRow()][cell1.getColumn()],
                model.getData()[cell2.getRow()][cell2.getColumn()]);
    }
}
