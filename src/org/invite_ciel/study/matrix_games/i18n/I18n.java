package org.invite_ciel.study.matrix_games.i18n;

import java.util.ResourceBundle;

/**
 * Created by InviteCiel on 19.03.16.
 */
public class I18n {
    private I18n() {}
    private static ResourceBundle bundle;

    public static String getString(String key) {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("label");
        }
        return bundle.getString(key);
    }
}
