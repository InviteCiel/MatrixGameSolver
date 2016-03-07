package org.invite_ciel.study.matrix_games.view;

/**
 * Created by InviteCiel on 29.02.16.
 */
public enum SolverFormState {
    DEFAULT(false, false, false, false, false),
    INCORRECT_INPUT(true, false, false, false, false),
    READY_TO_SOLVE(false, false, false, false, true),
    SOLVED_IN_PURE_STRATEGIES(false, true, true, false, false),
    SOLVED_IN_MIXED_STRATEGIES(false, true, false, true, false);

    boolean showIncorrectInputPanel;
    boolean showGameResultPanel;
    boolean showPureStrategyPanel;
    boolean showMixedStrategyPanel;
    boolean solveButtonEnabled;

    SolverFormState(boolean showIncorrectInputPanel,
                    boolean showGameResultPanel,
                    boolean showPureStrategyPanel,
                    boolean showMixedStrategyPanel,
                    boolean solveButtonEnabled) {
        this.showIncorrectInputPanel = showIncorrectInputPanel;
        this.showGameResultPanel = showGameResultPanel;
        this.showPureStrategyPanel = showPureStrategyPanel;
        this.showMixedStrategyPanel = showMixedStrategyPanel;
        this.solveButtonEnabled = solveButtonEnabled;
    }

    public boolean isShowIncorrectInputPanel() {
        return showIncorrectInputPanel;
    }

    public boolean isShowGameResultPanel() {
        return showGameResultPanel;
    }

    public boolean isShowPureStrategyPanel() {
        return showPureStrategyPanel;
    }

    public boolean isShowMixedStrategyPanel() {
        return showMixedStrategyPanel;
    }

    public boolean isSolveButtonEnabled() {
        return solveButtonEnabled;
    }
}
