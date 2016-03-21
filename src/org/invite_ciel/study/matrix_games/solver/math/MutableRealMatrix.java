package org.invite_ciel.study.matrix_games.solver.math;

import org.apache.commons.math3.linear.RealMatrix;
import org.invite_ciel.study.matrix_games.reduce.ReductionState;

/**
 * Created by InviteCiel on 28.02.16.
 */
public interface MutableRealMatrix extends RealMatrix {
    void removeRow(int del_row);
    void removeColumn(int del_col);
    ReductionState getReductionState();
    MutableRealMatrix reduce();
}
