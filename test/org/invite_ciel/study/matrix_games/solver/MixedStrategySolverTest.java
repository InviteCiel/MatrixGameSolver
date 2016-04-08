package org.invite_ciel.study.matrix_games.solver;

import org.invite_ciel.study.matrix_games.solver.math.MutableArray2DRowRealMatrix;
import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.MixedStrategyMatrixGameSolution;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * MixedStrategySolverTest
 *
 * @author Gleb Mayorov
 */
public class MixedStrategySolverTest {

    @Test
    public void solve() throws Exception {
        MixedStrategyMatrixGameSolution testSolution =
                new MixedStrategyMatrixGameSolution(
                        2.8,
                        new double[]{0.6, 0.0, 0.0, 0.4},
                        new double[]{0.6, 0.4}
                );
        MutableRealMatrix model = new MutableArray2DRowRealMatrix(new double[][]{
                {2, 4},
                {3, 2},
                {1, 3},
                {4, 1}
        });
        MixedStrategyMatrixGameSolution solution = MixedStrategySolver.solve(model);
        assertEquals(testSolution, solution);
    }
}