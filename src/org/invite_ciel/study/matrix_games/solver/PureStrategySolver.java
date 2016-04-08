package org.invite_ciel.study.matrix_games.solver;

import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.PureStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.utils.Cell;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * PureStrategySolver
 *
 * @author Gleb Mayorov
 */
public class PureStrategySolver {
    private PureStrategySolver() {
    }

    public static PureStrategyMatrixGameSolution solve(MutableRealMatrix model) {
        List<Cell> playerAPureStrategyCells = getPlayerAPureStrategyCells(model);
        List<Cell> playerBPureStrategyCells = getPlayerBPureStrategyCells(model);
        List<Cell> resultList = ListUtils.intersect(playerAPureStrategyCells, playerBPureStrategyCells);
        if (!resultList.isEmpty()) {
            return new PureStrategyMatrixGameSolution(resultList);
        } else return null;
    }

    static List<Cell> getPlayerAPureStrategyCells(MutableRealMatrix model) {
        double[][] data = model.getData();
        Stream<Stream<Cell>> rowStream = getCellStreamStreamFromTwoDimData(data);
        Stream<Map<Double, List<Cell>>> rowCellGroupingStream =
                rowStream.map(row -> row.collect(Collectors.groupingBy(Cell::getValue)));
        Stream<List<Cell>> minimumCellsInRowStream = rowCellGroupingStream.map(
                cellGrouping -> cellGrouping.entrySet().stream()
                        .min((o1, o2) -> Double.compare(o1.getKey(), o2.getKey())).get()
                        .getValue()
        );
        Map<Double, List<Cell>> minCells = minimumCellsInRowStream
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Cell::getValue));
        return minCells.entrySet().stream().max((o1, o2) -> Double.compare(o1.getKey(), o2.getKey())).get().getValue();
    }

    static List<Cell> getPlayerBPureStrategyCells(MutableRealMatrix model) {
        double[][] data = model.transpose().getData();
        Stream<Stream<Cell>> rowStream = getCellStreamStreamFromTwoDimData(data);
        Stream<Map<Double, List<Cell>>> rowCellGroupingStream =
                rowStream.map(row -> row.collect(Collectors.groupingBy(Cell::getValue)));
        Stream<List<Cell>> maximumCellsInRowStream = rowCellGroupingStream.map(
                cellGrouping -> cellGrouping.entrySet().stream()
                        .max((o1, o2) -> Double.compare(o1.getKey(), o2.getKey())).get()
                        .getValue()
        );
        Map<Double, List<Cell>> cellsGroupedByValue = maximumCellsInRowStream
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Cell::getValue));
        List<Cell> maxCells = cellsGroupedByValue.entrySet().stream()
                .min((o1, o2) -> Double.compare(o1.getKey(), o2.getKey())).get().getValue();
        return maxCells.stream().map(Cell::transposed).collect(Collectors.toList());
    }

    private static Stream<Stream<Cell>> getCellStreamStreamFromTwoDimData(double[][] data) {
        return IntStream.range(0, data.length).mapToObj(rowIndex ->
                IntStream.range(0, data[rowIndex].length).mapToObj(columnIndex ->
                        new Cell(rowIndex, columnIndex, data[rowIndex][columnIndex])
                )
        );
    }
}
