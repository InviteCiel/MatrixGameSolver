package org.invite_ciel.study.matrix_games.reduce;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.security.InvalidParameterException;
import java.util.Comparator;

/**
 * Created by InviteCiel on 17.03.16.
 */
public class StrategyComparator implements Comparator<RealVector>{
    private final boolean maxMin;
    public StrategyComparator(boolean isMaxMin) {
        maxMin = isMaxMin;
    }

    public static StrategyComparator createMinMaxStrategyComparator () {
        return new StrategyComparator(false);
    }

    public static StrategyComparator createMaxMinStrategyComparator () {
        return new StrategyComparator(true);
    }

    @Override
    public int compare(RealVector o1, RealVector o2) {
        if (o1.getDimension() != o2.getDimension())
            throw new InvalidParameterException("Vectors should have same dimensions!");
        boolean allLesserOrEqual = true,
                allGreaterOrEqual = true;
        for (int i = 0; i < o1.getDimension(); i++) {
            if (o1.getEntry(i) < o2.getEntry(i)) {
                allGreaterOrEqual = false;
            } else if (o1.getEntry(i) > o2.getEntry(i)) {
                allLesserOrEqual = false;
            }
        }
        int result = 0;
        if (allLesserOrEqual) {
            result = 1;
        } else if (allGreaterOrEqual) {
            result = -1;
        }
        if (maxMin) {
            result = - result;
        }
        return result;
    }

    public static void main(String[] args) {
        RealVector rv1 = new ArrayRealVector(new double[]{1, 2, 3});
        RealVector rv2 = new ArrayRealVector(new double[]{2, 3, 4});
        System.out.println(new StrategyComparator(false).compare(rv1, rv2));
    }
}
