package org.invite_ciel.study.matrix_games.math;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by InviteCiel on 28.02.16.
 */
public interface MutableRealMatrix extends RealMatrix {
    void removeRow(int del_row);
    void removeColumn(int del_col);
}
