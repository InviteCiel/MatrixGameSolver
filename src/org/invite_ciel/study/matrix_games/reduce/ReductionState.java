package org.invite_ciel.study.matrix_games.reduce;

import org.invite_ciel.study.matrix_games.i18n.I18n;

/**
 * Created by InviteCiel on 17.03.16.
 */
public enum ReductionState {
    NOT_REDUCABLE("notReducableDescription", false),
    REDUCABLE("reducableDescription", true),
    REDUCED("reducedDescription", false);

    private final String description;
    private final boolean showReduceButton;

    ReductionState (String descriptionKey,
                    boolean showReduceButton) {
        description = I18n.getString(descriptionKey);
        this.showReduceButton = showReduceButton;
    }

    public boolean isShowReduceButton() {
        return showReduceButton;
    }

    public String getDescription() {
        return description;
    }
}
