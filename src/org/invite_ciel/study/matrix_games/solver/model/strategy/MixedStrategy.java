package org.invite_ciel.study.matrix_games.solver.model.strategy;

/**
 * Created by InviteCiel on 06.03.16.
 */
public class MixedStrategy {
    private final double gamma;
    private final double[] probabilities;

    public MixedStrategy(double gamma, double[] probabilities) {
        this.gamma = gamma;
        this.probabilities = probabilities;
    }

    public double getGamma() {
        return gamma;
    }

    public double[] getProbabilities() {
        return probabilities;
    }
}
