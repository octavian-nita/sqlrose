package net.appfold.sqlrose.i18n;

import lombok.*;

import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.*;

import static java.lang.System.getProperty;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.util.Optional.*;
import static java.util.ResourceBundle.getBundle;
import static net.appfold.sqlrose.cache.MemoizedBiFn.memoize;
import static org.apache.commons.lang3.StringUtils.appendIfMissing;

/**
 * A fairly <em>simple</em> implementation of {@link I18n}.
 * <p/>
 * Very similar in behaviour to <code><a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ResourceBundleMessageSource.html">
 * Spring's ResourceBundleMessageSource</a></code>, this class relies on the underlying JDK's {@link ResourceBundle}
 * implementation in combination with the JDK's standard message parsing provided by {@link MessageFormat}, {@link
 * net.appfold.sqlrose.cache.SoftCache (soft-)caches} by default the generated message formats for each message and can
 * be configured concerning how both the {@link #setResourceBundleSupplier(BiFunction) resource bundles} and the {@link
 * #setMessageFormatSupplier(BiFunction) message formats} are created. It also suffers from the same shortcomings as
 * {@code ResourceBundleMessageSource} so a {@code ReloadableResourceBundleMessageSource} might be a better choice if
 * the client application is based on Spring.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, May 23, 2019
 * @see <a href="https://blog.joda.org/2011/08/implementations-of-interfaces-prefixes.html">Implementations of
 *     interfaces - prefixes and suffixes</a>
 */
@Builder(toBuilder = true)
public class SimpleI18n implements I18n {

    public static final String L10N_BASEDIR_KEY = "l10n.basedir";

    public static final String L10N_BASEDIR_DEF = "l10n/";

    @Singular
    protected final Set<String> bundleBaseNames = new LinkedHashSet<>(4);

    @Builder.Default
    protected final String bundlePrefix = appendIfMissing(getProperty(L10N_BASEDIR_KEY, L10N_BASEDIR_DEF).trim(), "/");

    @Builder.Default
    protected final Locale locale = Locale.getDefault();

    @Builder.Default
    protected final BiFunction<String, Locale, MessageFormat> messageFormatSupplier = memoize(MessageFormat::new);

    @Builder.Default
    protected final BiFunction<String, Locale, ResourceBundle> resourceBundleSupplier =
        (baseName, locale) -> locale == null ? getBundle(baseName) : getBundle(baseName, locale);

    @Builder.Default
    protected final DateTimeFormatter dateTimeFormatter = RFC_1123_DATE_TIME;

    @Builder.Default
    protected final ZoneId zoneId = ZoneId.systemDefault();

    @Override
    public Locale getLocale() { return locale; }

    @Override
    public DateTimeFormatter getDateTimeFormatter() { return dateTimeFormatter; }

    @Override
    public ZoneId getZoneId() { return zoneId; }

    @NonNull
    @Override
    public String t(String key, Object... args) {
        if (key == null) {
            return "";
        }

        final Locale locale = firstNonNull(this::getLocale, this::getDefaultLocale).orElseGet(Locale::getDefault);

        final String message = message(key, locale);
        if (args == null || args.length == 0 || messageFormatSupplier == null) {
            return message;
        }

        final MessageFormat format = messageFormatSupplier.apply(message, locale);
        return format == null ? message : format.format(args);
    }

    @NonNull
    protected String message(@NonNull String key, @NonNull Locale locale) {
        if (resourceBundleSupplier == null) {
            return key;
        }

        for (String baseName : bundleBaseNames) {
            if (baseName == null) {
                continue;
            }

            if (bundlePrefix != null) {
                baseName = bundlePrefix + baseName;
            }

            try {
                final ResourceBundle resourceBundle = resourceBundleSupplier.apply(baseName, locale);
                if (resourceBundle != null) {
                    return resourceBundle.getString(key);
                }
            } catch (MissingResourceException ex) {
                // continue to the next bundle (base name)
            }
        }

        return key;
    }

    @NonNull
    @Override
    public String l(Instant instant) {
        if (instant == null) {
            return "";
        }

        final DateTimeFormatter dtf =
            firstNonNull(this::getDateTimeFormatter, this::getDefaultDateTimeFormatter).orElse(RFC_1123_DATE_TIME);
        final Locale locale = firstNonNull(this::getLocale, this::getDefaultLocale).orElseGet(Locale::getDefault);
        final ZoneId zoneId = firstNonNull(this::getZoneId, this::getDefaultZoneId).orElseGet(ZoneId::systemDefault);

        return dtf.withLocale(locale).format(instant.atZone(zoneId));
    }

    @NonNull
    @SafeVarargs
    protected static <T> Optional<T> firstNonNull(Supplier<T>... suppliers) {
        if (suppliers != null) {
            for (Supplier<T> supplier : suppliers) {
                if (supplier != null) {
                    final T t = supplier.get();
                    if (t != null) {
                        return of(t);
                    }
                }
            }
        }
        return empty();
    }
}
