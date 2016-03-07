package org.invite_ciel.study.matrix_games.solver.model.solution;

import java.util.Arrays;

/**
 * Created by InviteCiel on 02.03.16.
 */
public final class MixedStrategyMatrixGameSolution extends MatrixGameSolution {
    private final double[] playerBMixedStrategy;
    private final double[] playerAMixedStrategy;

    public MixedStrategyMatrixGameSolution(double gamma, double[] playerAMixedStrategy, double[] playerBMixedStrategy) {
        super(gamma);
        this.playerAMixedStrategy = playerAMixedStrategy;
        this.playerBMixedStrategy = playerBMixedStrategy;
    }

    @Override
    public String getPlayerAStrategyView() {
        return getMixedStrategyView(playerAMixedStrategy);
    }

    @Override
    public String getPlayerBStrategyView() {
        return getMixedStrategyView(playerBMixedStrategy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MixedStrategyMatrixGameSolution that = (MixedStrategyMatrixGameSolution) o;

        return Arrays.equals(playerAMixedStrategy, that.playerAMixedStrategy) &&
                Arrays.equals(playerBMixedStrategy, that.playerBMixedStrategy);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(playerBMixedStrategy);
        result = 31 * result + Arrays.hashCode(playerAMixedStrategy);
        return result;
    }

    private String getMixedStrategyView(double[] mixedStrategy) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (int i = 0; i < mixedStrategy.length - 1; i++) {
            builder.append(mixedStrategy[i]);
            builder.append(", ");
        }
        builder.append(mixedStrategy[mixedStrategy.length - 1]);
        builder.append(")");
        return builder.toString();
    }
}
