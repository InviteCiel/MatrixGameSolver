package org.invite_ciel.study.matrix_games.solver.math;

import org.apache.commons.math3.linear.RealMatrix;
import org.invite_ciel.study.matrix_games.reduce.ReductionState;
import org.invite_ciel.study.matrix_games.solver.model.utils.Cell;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by InviteCiel on 28.02.16.
 */
public interface MutableRealMatrix extends RealMatrix {
    void removeRow(int del_row);
    void removeColumn(int del_col);
    ReductionState getReductionState();
    MutableRealMatrix reduce();

    default Stream<Stream<Cell>> getAsStreamOfRowCellStreams() {
        double[][] modelArray = getData();
        return IntStream.range(0, modelArray.length).mapToObj(
                rowIndex -> IntStream.range(0, modelArray[rowIndex].length)
                        .mapToObj(columnIndex -> new Cell(rowIndex, columnIndex, modelArray[rowIndex][columnIndex]))
        );
    }

    MutableRealMatrix transpose();
}
