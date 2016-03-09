package org.invite_ciel.study.matrix_games.solver.model.solution;

/**
 * Created by InviteCiel on 02.03.16.
 */
public class PureStrategyMatrixGameSolution extends MatrixGameSolution {
    private final int playerAPureStrategy;
    private final int playerBPureStrategy;

    public PureStrategyMatrixGameSolution(double gamma, int playerAPureStrategy, int playerBPureStrategy) {
        super(gamma);
        this.playerAPureStrategy = playerAPureStrategy;
        this.playerBPureStrategy = playerBPureStrategy;
    }

    @Override
    public String getPlayerAStrategyView() {
        return (playerAPureStrategy + 1) + "";
    }

    @Override
    public String getPlayerBStrategyView() {
        return (playerBPureStrategy + 1) + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PureStrategyMatrixGameSolution that = (PureStrategyMatrixGameSolution) o;

        return playerAPureStrategy == that.playerAPureStrategy && playerBPureStrategy == that.playerBPureStrategy;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + playerAPureStrategy;
        result = 31 * result + playerBPureStrategy;
        return result;
    }
}
