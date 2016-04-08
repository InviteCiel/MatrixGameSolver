package org.invite_ciel.study.matrix_games.solver;

import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.MatrixGameSolution;

/**
 * Solver
 * @author Gleb Mayorov
 */
public class Solver {
    private final MutableRealMatrix model;

    public Solver(MutableRealMatrix model) {
        this.model = model;
    }

    public MatrixGameSolution solve() {
        MatrixGameSolution solution = PureStrategySolver.solve(model);
        if (solution == null)
            solution = MixedStrategySolver.solve(model);
        return solution;
    }
}
