package net.appfold.sqlrose.i18n;

import lombok.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Entry point to / façade of a basic internationalization subsystem which:
 * <ul>
 * <li><em>groups</em> parameters which play a part in consistently internationalizing the application, e.g., {@link
 * #getLocale() locale}, {@link #getDateTimeFormatter() date/time formatter}, {@link #getZoneId() time-zone}, etc.</li>
 * <li><em>outlines</em> a set of essential primitives which can be used to internationalize the application,
 * regardless of the actual implementation mechanism or framework employed ({@link java.util.ResourceBundle}-based, <a
 * href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ReloadableResourceBundleMessageSource.html">
 * Spring</a>-based, etc.)</li>
 * <li>employs shorter names for commonly used routines, e.g., {@link #t(String, Object...) t} for
 * <em>translating</em> messages, {@link #l(Instant) l} for <em>localizing</em> values like dates and numbers,
 * etc.</li>
 * </ul>
 * {@code I18n} instances can, for example, be injected/set up to represent user profile or request-specific,
 * (i18n-related) configuration settings.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, May 23, 2019
 * @see <a href="https://stackoverflow.com/a/10571144/272939">This</a> Stack Overflow answer
 */
public interface I18n {

    /**
     * @return the {@link Locale locale} currently used by {@code this} entry point; defaults to {@link #getDefaultLocale()}
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

    default DateTimeFormatter getDateTimeFormatter() {
        return getDefaultDateTimeFormatter();
    }

    default DateTimeFormatter getDefaultDateTimeFormatter() {
        return DateTimeFormatter.RFC_1123_DATE_TIME;
    }

    default ZoneId getZoneId() {
        return getDefaultZoneId();
    }

    default ZoneId getDefaultZoneId() {
        return ZoneId.systemDefault();
    }

    /**
     * If no message can be found/resolved for the given {@code key} and {@code #getLocale() currently used locale},
     * and depending on the actual implementation, this method could return the key itself (if non-{@code null}), an
     * empty string, etc.
     *
     * @return never {@code null}
     */
    @NonNull String t(String key, Object... args);

    default @NonNull String t(Object key, Object... args) {
        return t(key == null ? null : key.toString(), args);
    }

    /**
     * @return never {@code null} (at worst, an empty string)
     */
    @NonNull String l(Instant instant);
}
