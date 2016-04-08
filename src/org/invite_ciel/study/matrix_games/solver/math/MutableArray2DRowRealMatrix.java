package org.invite_ciel.study.matrix_games.solver.math;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.invite_ciel.study.matrix_games.reduce.ReductionState;
import org.invite_ciel.study.matrix_games.reduce.StrategyComparator;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Created by InviteCiel on 28.02.16.
 */
public class MutableArray2DRowRealMatrix extends Array2DRowRealMatrix implements MutableRealMatrix {
    private static final long serialVersionUID = 4231301860770686298L;

    private ReductionState reductionState = isReducable()?ReductionState.REDUCABLE:ReductionState.NOT_REDUCABLE;

    private Field dataField;

    public MutableArray2DRowRealMatrix() {
        super();
        gainAccessToAncestorDataField();
    }

    public MutableArray2DRowRealMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        super(rowDimension, columnDimension);
        gainAccessToAncestorDataField();
    }

    public MutableArray2DRowRealMatrix(double[][] d) throws DimensionMismatchException, NoDataException, NullArgumentException {
        super(d);
        gainAccessToAncestorDataField();
    }

    public MutableArray2DRowRealMatrix(double[][] d, boolean copyArray) throws DimensionMismatchException, NoDataException, NullArgumentException {
        super(d, copyArray);
        gainAccessToAncestorDataField();
    }

    public MutableArray2DRowRealMatrix(double[] v) {
        super(v);
        gainAccessToAncestorDataField();
    }

    private void gainAccessToAncestorDataField() {
        try {
            dataField = getClass().getSuperclass().getDeclaredField("data");
            dataField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeRow(int del_row) {
        if (getRowDimension() <= 2)
            throw new IllegalArgumentException("Minimum matrix size should be 2*2!");
        try {
            dataField.set(this, ArrayUtils.remove((double[][]) dataField.get(this), del_row));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeColumn(int del_col) {
        if (getColumnDimension() <= 2)
            throw new IllegalArgumentException("Minimum matrix size should be 2*2!");
        try {
            double[][] data = (double[][])dataField.get(this);
            for (int i = 0; i < this.getRowDimension(); i++) {
                data[i] =  ArrayUtils.remove(data[i], del_col);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReductionState getReductionState() {
        return reductionState;
    }

    @Override
    public MutableRealMatrix reduce() {
        MutableArray2DRowRealMatrix result = new MutableArray2DRowRealMatrix(getData());
        while (result.isReducable()) {
            while (result.isReducableByRow()) {
                result.removeRow(getFirstDominatedRow());
            }
            while (result.isReducableByColumn()) {
                result.removeColumn(getFirstDominatedColumn());
            }
        }
        result.reductionState = ReductionState.REDUCED;
        return result;
    }


    private int getFirstDominatedRow() {
        Comparator<RealVector> comparator = new StrategyComparator(true);
        for (int firstRowIndex = 0; firstRowIndex < getRowDimension(); firstRowIndex++) {
            for (int secondRowIndex = firstRowIndex + 1; secondRowIndex < getRowDimension(); secondRowIndex++) {
                int compared = comparator.compare(getRowVector(firstRowIndex), getRowVector(secondRowIndex));
                if (compared < 0) {
                    return firstRowIndex;
                } else if (compared > 0) {
                    return secondRowIndex;
                }
            }
        }
        return -1;
    }

    private int getFirstDominatedColumn() {
        Comparator<RealVector> comparator = new StrategyComparator(false);
        for (int firstColumnIndex = 0; firstColumnIndex < getColumnDimension(); firstColumnIndex++) {
            for (int secondColumnIndex = firstColumnIndex + 1; secondColumnIndex < getColumnDimension(); secondColumnIndex++) {
                int compared = comparator.compare(getColumnVector(firstColumnIndex), getColumnVector(secondColumnIndex));
                if (compared < 0) {
                    return firstColumnIndex;
                } else if (compared > 0) {
                    return secondColumnIndex;
                }
            }
        }
        return -1;
    }

    private void setData(double [][] data) {
        try {
            dataField.set(this, data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MutableArray2DRowRealMatrix{\n");
        double [][] data = getData();

        for (double [] raw: data) {
            builder.append("\t");
            for (double cell: raw) {
                builder.append(cell);
                builder.append("\t");
            }
            builder.append("\n");
        }

        builder.append("}");
        return builder.toString();
    }

    private boolean isReducable() {
        return getRowDimension() > 2 &&
                getColumnDimension() > 2 &&
                (getFirstDominatedRow() != -1 || getFirstDominatedColumn() != -1);
    }

    private boolean isReducableByRow() {
        return getRowDimension() > 2 &&
                getFirstDominatedRow() != -1;
    }

    private boolean isReducableByColumn() {
        return getColumnDimension() > 2 &&
                getFirstDominatedColumn() != -1;
    }

    @Override
    public MutableRealMatrix transpose() {
        return new MutableArray2DRowRealMatrix(super.transpose().getData());
    }
}
