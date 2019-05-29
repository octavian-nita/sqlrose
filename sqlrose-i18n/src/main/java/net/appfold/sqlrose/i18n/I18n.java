package net.appfold.sqlrose.i18n;

import lombok.NonNull;

import java.util.Locale;

/**
 * Entry point/façade for a basic internationalization subsystem. Employs shorter names for commonly used routines
 * e.g., {@link #t(String, Object...) t} for <em>translating</em> messages, <em>l</em> for <em>localizing</em> values
 * like dates and numbers, etc.
 * <p/>
 * The purpose here is to <em>outline</em> a set of essential primitives used to internationalize the application,
 * regardless of the actual implementation mechanism or framework employed ({@link java.util.ResourceBundle}-based, <a
 * href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ReloadableResourceBundleMessageSource.html">
 * Spring</a>-based, etc.).
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, May 23, 2019
 */
public interface I18n {

    /**
     * @return the {@link Locale locale} currently used by {@code this} entry point; (appropriately) defaults to
     *     {@link #getDefaultLocale()}
     */
    default Locale getLocale() {
        return getDefaultLocale();
    }

    /**
     * @return the {@link Locale locale} used by {@code this} entry point if no other locale has been set; defaults to
     *     {@code Locale.getDefault()}
     */
    default Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    /**
     * Defaults to returning {@code t(getLocale(), key, args)}.
     */
    @NonNull
    default String t(String key, Object... args) {
        return t(getLocale(), key, args);
    }

    /**
     * If no message can be found/resolved for the given {@code key} and {@code locale}, depending on the actual
     * implementation, this method could return the key itself (if non-{@code null}) or the empty string, etc.
     *
     * @return never {@code null}
     */
    @NonNull String t(Locale locale, String key, Object... args);
}
