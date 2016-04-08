package org.invite_ciel.study.matrix_games.solver;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.MixedStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.strategy.MixedStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * MixedStrategySolver
 *
 * @author Gleb Mayorov
 */
public class MixedStrategySolver {
    private MixedStrategySolver() {
    }

    public static MixedStrategyMatrixGameSolution solve(MutableRealMatrix model) {
        MixedStrategy firstPlayerMS = getPlayerAMixedStrategy(model);
        MixedStrategy secondPlayerMS = getPlayerBMixedStrategy(model);
        return new MixedStrategyMatrixGameSolution(
                firstPlayerMS.getGamma(),
                firstPlayerMS.getProbabilities(),
                secondPlayerMS.getProbabilities());
    }

    private static double getMinimumCellValue(MutableRealMatrix model) {
        return Arrays.stream(model.getData()).flatMapToDouble(Arrays::stream).min().getAsDouble();
    }

    private static MixedStrategy getPlayerAMixedStrategy(MutableRealMatrix model) {
        return simplexMethod(model.transpose(),
                Relationship.GEQ,
                GoalType.MINIMIZE);
    }


    private static MixedStrategy getPlayerBMixedStrategy(MutableRealMatrix model) {
        return simplexMethod(model,
                Relationship.LEQ,
                GoalType.MAXIMIZE
        );
    }

    private static MixedStrategy simplexMethod(MutableRealMatrix model, Relationship constraintRelationship, GoalType goalType) {
        double minimumCellValue = getMinimumCellValue(model);
        LinearObjectiveFunction playerObjectiveFunction = getLinearObjectiveFunction(model);
        LinearConstraintSet constraintSet = getLinearConstraintSet(model, constraintRelationship, minimumCellValue);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solutionPair = solver.optimize(
                new MaxIter(100), playerObjectiveFunction, constraintSet, goalType, new NonNegativeConstraint(true));

        double gamePrice = getGamePrice(minimumCellValue, solutionPair);
        double[] strategyProbabilities = getStrategyProbabilities(solutionPair);
        return new MixedStrategy(gamePrice, strategyProbabilities);
    }

    private static LinearConstraintSet getLinearConstraintSet(RealMatrix model, Relationship constraintRelationship, double minimumCellValue) {
        List<LinearConstraint> constraintList = Arrays.stream(model.getData())
                .map(doubles -> new LinearConstraint(
                        Arrays.stream(doubles).map(cellValue -> cellValue - minimumCellValue).toArray(),
                        constraintRelationship,
                        1))
                .collect(Collectors.toList());
        return new LinearConstraintSet(constraintList);
    }

    private static LinearObjectiveFunction getLinearObjectiveFunction(RealMatrix model) {
        double[] functionCoefficients = IntStream.generate(() -> 1).limit(model.getColumnDimension()).asDoubleStream().toArray();
        return new LinearObjectiveFunction(functionCoefficients, 0);
    }

    private static double getGamePrice(double minimumCellValue, PointValuePair solutionPair) {
        return (1 / solutionPair.getValue()) + minimumCellValue;
    }

    private static double[] getStrategyProbabilities(PointValuePair solutionPair) {
        return Arrays.stream(solutionPair.getPoint()).map(
                operand -> operand / solutionPair.getValue()).toArray();
    }
}
