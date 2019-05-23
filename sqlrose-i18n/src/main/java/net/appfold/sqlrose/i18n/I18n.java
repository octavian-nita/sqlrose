package net.appfold.sqlrose.i18n;

import lombok.NonNull;

import java.util.Locale;

/**
 * Entry point/façade for a basic internationalization subsystem. Employs {@link #t(String, Object...) shorter}
 * {@link #t(Locale, String, Object...) names} for commonly used methods ;) (<em>t</em> for <em>translate</em>,
 * <em>l</em> for <em>localize</em>, etc.)
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, May 23, 2019
 */
public interface I18n {

    default Locale getLocale() {
        return getDefaultLocale();
    }

    default Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    @NonNull
    default String t(String key, Object... args) {
        return t(getLocale(), key, args);
    }

    @NonNull String t(Locale locale, String key, Object... args);
}
