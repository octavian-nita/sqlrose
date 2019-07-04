package net.appfold.sqlrose.i18n;

import lombok.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Entry point to / façade of a basic internationalization subsystem that:
 * <ul>
 * <li>groups <em>parameters</em> which play a part in consistently internationalizing the application, e.g., {@link
 * #getLocale() locale}, {@link #getDateTimeFormatter() date/time formatter}, {@link #getZoneId() time-zone}, etc.</li>
 * <li>outlines a set of essential <em>primitives</em> which can be used to internationalize the application,
 * regardless of the actual implementation mechanism or framework employed ({@link java.util.ResourceBundle
 * ResourceBundle}-based, <a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ReloadableResourceBundleMessageSource.html">
 * Spring</a>-based, etc.)</li>
 * <li>employs <em>shorter names</em> for commonly used routines, e.g., {@link #t(String, Object...) t} for
 * <em>translating</em> messages, {@link #l(Instant) l} for <em>localizing</em> values like dates and numbers,
 * etc.</li>
 * </ul>
 * {@code I18n} instances can, for example, be set up/injected to represent user profile or request-specific,
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
     * @return the {@link Locale locale} used by default by the application; defaults to {@code Locale.getDefault()}
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

    default @NonNull String t(Object key, Object... args) {
        return t(key == null ? null : key.toString(), args);
    }

    /**
     * If no message can be found/resolved for the given {@code key} and {@link #getLocale() currently used locale},
     * depending on the actual implementation, this method could return the key itself (if non-{@code null}), an empty
     * string, etc.
     *
     * @return never {@code null}
     */
    @NonNull String t(String key, Object... args);

    /**
     * @return never {@code null} (at worst, an empty string)
     */
    @NonNull String l(Instant instant);
}
