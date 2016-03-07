package org.invite_ciel.study.matrix_games.solver.model.solution;

/**
 * Created by InviteCiel on 01.03.16.
 */
public abstract class MatrixGameSolution {
    private final double gamma;
    protected MatrixGameSolution(double gamma) {
        this.gamma = gamma;
    }

    public String getGameResultView() {
        return gamma + "";
    }
    public abstract String getPlayerAStrategyView();
    public abstract String getPlayerBStrategyView();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatrixGameSolution that = (MatrixGameSolution) o;

        return Double.compare(that.gamma, gamma) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(gamma);
        return (int) (temp ^ (temp >>> 32));
    }
}
