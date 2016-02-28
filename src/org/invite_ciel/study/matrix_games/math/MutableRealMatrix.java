package org.invite_ciel.study.matrix_games.math;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by InviteCiel on 28.02.16.
 */
public interface MutableRealMatrix extends RealMatrix {
    void deleteRow(int del_row);
    void deleteColumn(int del_col);
}
