package org.invite_ciel.study.matrix_games.solver;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.MatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.solution.MixedStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.solution.PureStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.strategy.MixedStrategy;
import org.invite_ciel.study.matrix_games.solver.model.utils.CellIndex;
import org.invite_ciel.study.matrix_games.solver.model.utils.CellIndexComparator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by InviteCiel on 01.03.16.
 */
public class Solver {
    private final MutableRealMatrix model;
    private final CellIndex playerAPureStrategyCellIndex, playerBPureStrategyCellIndex;

    public Solver(MutableRealMatrix model) {
        this.model = model;
        playerAPureStrategyCellIndex = getPlayerAPureStrategyCellIndex();
        playerBPureStrategyCellIndex = getPlayerBPureStrategyCellIndex();

    }

    public MatrixGameSolution solve() {
        MatrixGameSolution solution = solveInPureStrategies();
        if (solution == null)
            solution = solveInMixedStrategies();
        return solution;
    }

    MatrixGameSolution solveInPureStrategies() {
        double minimumGamePrice = minimumGamePrice();
        if (minimumGamePrice == maximumGamePrice()) {
            return new PureStrategyMatrixGameSolution(minimumGamePrice,
                    getEquivalentCells(minimumGamePrice));
        } else return null;
    }

    private List<CellIndex> getEquivalentCells(double value) {
        return IntStream.range(0, model.getData().length).mapToObj(
                rowIndex -> IntStream.range(0, model.getData()[0].length)
                        .filter(columnIndex -> model.getEntry(rowIndex, columnIndex) == value)
                        .mapToObj(columnIndex -> new CellIndex(rowIndex, columnIndex))
        ).flatMap(cellIndexStream -> cellIndexStream).collect(Collectors.toList());
    }

    private MatrixGameSolution solveInMixedStrategies() {
        double minimumCellValue = Arrays.stream(model.getData()).flatMapToDouble(Arrays::stream).min().getAsDouble();
        MixedStrategy firstPlayerMS = getPlayerAMixedStrategy(minimumCellValue);
        MixedStrategy secondPlayerMS = getPlayerBMixedStrategy(minimumCellValue);
        return new MixedStrategyMatrixGameSolution(
                firstPlayerMS.getGamma(),
                firstPlayerMS.getProbabilities(),
                secondPlayerMS.getProbabilities());
    }

    private MixedStrategy getPlayerAMixedStrategy(double minimumCellValue) {
        return simplexMethod(model.transpose(), Relationship.GEQ, GoalType.MINIMIZE, minimumCellValue);
    }


    private MixedStrategy getPlayerBMixedStrategy(double minimumCellValue) {
        return simplexMethod(model, Relationship.LEQ, GoalType.MAXIMIZE, minimumCellValue);
    }

    private MixedStrategy simplexMethod (RealMatrix model, Relationship constraintRelationship, GoalType goalType, double minimumCellValue) {
        double[] functionCoefficients = IntStream.generate(() -> 1).limit(model.getColumnDimension()).asDoubleStream().toArray();
        LinearObjectiveFunction playerAFunction = new LinearObjectiveFunction(functionCoefficients, 0);
        List<LinearConstraint> constraintList = Arrays.stream(model.getData())
                .map(doubles -> new LinearConstraint(
                        Arrays.stream(doubles).map(cellValue -> cellValue - minimumCellValue).toArray(),
                        constraintRelationship,
                        1))
                .collect(Collectors.toList());

        LinearConstraintSet constraintSet = new LinearConstraintSet(constraintList);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solutionPair = solver.optimize(
                new MaxIter(100), playerAFunction, constraintSet, goalType, new NonNegativeConstraint(true));
        return new MixedStrategy((1/solutionPair.getValue()) + minimumCellValue, Arrays.stream(solutionPair.getPoint()).map(
                operand -> operand / solutionPair.getValue()).toArray());
    }

    private CellIndex getPlayerAPureStrategyCellIndex() {
        return IntStream.range(0, model.getData().length).mapToObj(rowIndex ->
                        new CellIndex(
                                rowIndex,
                                IntStream.range(0, model.getData()[0].length)
                                        .reduce((leftColumnCellIndex, rightColumnCellIndex) ->
                                                (model.getData()[rowIndex][leftColumnCellIndex] <
                                                        model.getData()[rowIndex][rightColumnCellIndex]) ?
                                                        leftColumnCellIndex : rightColumnCellIndex).getAsInt()
                        )
        ).max(new CellIndexComparator(model)).get();
    }

    private CellIndex getPlayerBPureStrategyCellIndex() {
        return IntStream.range(0, model.getData()[0].length).mapToObj(columnIndex ->
                        new CellIndex(
                                IntStream.range(0, model.getData().length).reduce((topRowCellIndex, bottomRowCellIndex) ->
                                                model.getData()[topRowCellIndex][columnIndex] >
                                                        model.getData()[bottomRowCellIndex][columnIndex] ?
                                                        topRowCellIndex : bottomRowCellIndex
                                ).getAsInt(),
                                columnIndex
                        )
        ).min(new CellIndexComparator(model)).get();
    }

    private double minimumGamePrice() {
        return model.getData()[playerAPureStrategyCellIndex.getRow()][playerAPureStrategyCellIndex.getColumn()];
    }

    private double maximumGamePrice() {
        return model.getData()[playerBPureStrategyCellIndex.getRow()][playerBPureStrategyCellIndex.getColumn()];
    }
}
