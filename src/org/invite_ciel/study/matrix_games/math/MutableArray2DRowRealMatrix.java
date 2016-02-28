package org.invite_ciel.study.matrix_games.math;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by InviteCiel on 28.02.16.
 */
public class MutableArray2DRowRealMatrix extends Array2DRowRealMatrix implements MutableRealMatrix {
    private static final long serialVersionUID = 4231301860770686298L;

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
    public void deleteRow(int del_row) {
        try {
            dataField.set(this, ArrayUtils.remove((double[][]) dataField.get(this), del_row));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteColumn(int del_col) {
        try {
            double[][] data = (double[][])dataField.get(this);
            for (int i = 0; i < this.getRowDimension(); i++) {
                data[i] =  ArrayUtils.remove(data[i], del_col);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
