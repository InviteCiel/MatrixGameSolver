package org.invite_ciel.study.matrix_games.solver;

import org.invite_ciel.study.matrix_games.solver.math.MutableArray2DRowRealMatrix;
import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.PureStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.utils.Cell;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * PureStrategySolverTest
 *
 * @author Gleb Mayorov
 */
public class PureStrategySolverTest {

    @Test
    public void solve() throws Exception {
        PureStrategyMatrixGameSolution testSolution = new PureStrategyMatrixGameSolution(
                Collections.singletonList(new Cell(1, 2, 1.0)));
        MutableRealMatrix model = new MutableArray2DRowRealMatrix(new double[][]{
                {5, 3, -1, 5},
                {5, 6, 1, 2},
                {4, 2, 0, 2}
        });
        PureStrategyMatrixGameSolution solution = PureStrategySolver.solve(model);
        assertEquals(testSolution, solution);
    }

    @Test
    public void getPlayerAPureStrategyCells() throws Exception {
        List<Cell> testAList = new LinkedList<>(Collections.singletonList(new Cell(1, 2, 1.0)));
        MutableRealMatrix model = new MutableArray2DRowRealMatrix(new double[][]{
                {5, 3, -1, 5},
                {5, 6, 1, 2},
                {4, 2, 0, 2}
        });
        List<Cell> playerAPureStrategiesList = PureStrategySolver.getPlayerAPureStrategyCells(model);
        assertEquals(testAList, playerAPureStrategiesList);
    }

    @Test
    public void getPlayerBPureStrategyCells() throws Exception {
        List<Cell> testAList = new LinkedList<>(Collections.singletonList(new Cell(1, 2, 1.0)));
        MutableRealMatrix model = new MutableArray2DRowRealMatrix(new double[][]{
                {5, 3, -1, 5},
                {5, 6, 1, 2},
                {4, 2, 0, 2}
        });
        List<Cell> playerBPureStrategiesList = PureStrategySolver.getPlayerBPureStrategyCells(model);
        assertEquals(testAList, playerBPureStrategiesList);
    }
}