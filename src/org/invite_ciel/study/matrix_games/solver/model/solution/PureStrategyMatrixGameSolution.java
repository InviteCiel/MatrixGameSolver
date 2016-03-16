package org.invite_ciel.study.matrix_games.solver.model.solution;

import org.invite_ciel.study.matrix_games.solver.model.utils.CellIndex;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by InviteCiel on 02.03.16.
 */
public class PureStrategyMatrixGameSolution extends MatrixGameSolution {
    private final List<CellIndex> saddlePoints;

    public PureStrategyMatrixGameSolution(double gamma, int playerAPureStrategy, int playerBPureStrategy) {
        super(gamma);
        saddlePoints = new LinkedList<>();
        saddlePoints.add(new CellIndex(playerAPureStrategy, playerBPureStrategy));
    }

    public PureStrategyMatrixGameSolution(double gamma, List<CellIndex> saddlePoints) {
        super(gamma);
        this.saddlePoints = saddlePoints;
    }

    private int getPlayerAStrategy() {
        return saddlePoints.get(0).getRow();
    }

    private int getPlayerBStrategy() {
        return saddlePoints.get(0).getColumn();
    }

    public String getSaddlePointsView() {
        AtomicInteger index = new AtomicInteger(0);
        return saddlePoints.stream().map(cellIndex -> new CellIndex(cellIndex.getRow() + 1, cellIndex.getColumn() + 1))
                .map(CellIndex::toString).collect(Collectors.joining(", "));
    }

    @Override
    public String getPlayerAStrategyView() {
        return (getPlayerAStrategy() + 1) + "";
    }

    @Override
    public String getPlayerBStrategyView() {
        return (getPlayerBStrategy() + 1) + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PureStrategyMatrixGameSolution that = (PureStrategyMatrixGameSolution) o;

        return getPlayerAStrategy() == that.getPlayerAStrategy() &&
                getPlayerBStrategy() == that.getPlayerBStrategy();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getPlayerAStrategy();
        result = 31 * result + getPlayerBStrategy();
        return result;
    }
}
