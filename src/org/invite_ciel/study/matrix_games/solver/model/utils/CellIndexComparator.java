package org.invite_ciel.study.matrix_games.solver.model.utils;

import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;

import java.util.Comparator;

/**
 * Created by InviteCiel on 05.03.16.
 */
public class CellIndexComparator implements Comparator<CellIndex> {
    private final MutableRealMatrix model;

    public CellIndexComparator(MutableRealMatrix model) {
        this.model = model;
    }

    @Override
    public int compare(CellIndex cellIndex1, CellIndex cellIndex2) {
        return Double.compare(model.getData()[cellIndex1.getRow()][cellIndex1.getColumn()],
                model.getData()[cellIndex2.getRow()][cellIndex2.getColumn()]);
    }
}
