package org.invite_ciel.study.matrix_games;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Temp class to study Commons Math3 SimplexSolver
 */
public class SolverController implements Runnable {
    final static double[] EXAMP_LIN_OBJ_FUNC_VECTOR = {2, 2, 1};
    final static double[][] CONSTRAINTS = {
            {1, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
    };

    public static void main(String[] args) {
        SolverController sc = new SolverController();
        sc.run();
    }

    @Override
    public void run() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(EXAMP_LIN_OBJ_FUNC_VECTOR, 0);
        Collection<LinearConstraint> constraints = new LinkedList<>();
        for (double[] constaint: CONSTRAINTS) {
            constraints.add(new LinearConstraint(constaint, Relationship.GEQ, 1));
        }
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new MaxIter(100), objectiveFunction,
                new LinearConstraintSet(constraints), GoalType.MINIMIZE, new NonNegativeConstraint(true));
        System.out.println(Arrays.toString(solution.getPoint()));
    }
}
