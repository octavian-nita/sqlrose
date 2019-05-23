package net.appfold.sqlrose.i18n;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.*;

import static java.lang.System.getProperty;
import static java.util.Collections.*;
import static java.util.Optional.*;

/**
 * A fairly <em>simple</em> implementation of {@link I18n} using the <em>
 * <a href="https://en.wikipedia.org/wiki/Curiously_recurring_template_pattern">curiously recurring template pattern</a>
 * </em>.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, May 23, 2019
 * @see <a href="https://blog.joda.org/2011/08/implementations-of-interfaces-prefixes.html">Implementations of
 *     interfaces - prefixes and suffixes</a>
 * @see <a href="https://stackoverflow.com/a/23895571/272939">Answer to <em>Fluent API with inheritance and
 *     generics</em></a>
 * @see <a href="https://stackoverflow.com/a/7355094/272939">Answer to <em>Is there a way to refer to the current type
 *     with a type variable?</em></a>
 */
@Slf4j
public class SimpleI18n<SELF extends SimpleI18n<SELF>> implements I18n {

    public static final String L10N_BASEDIR_KEY = "l10n.basedir";

    public static final String L10N_BASEDIR_DEF = "l10n/";

    protected final Set<String> bundleBaseNames = new LinkedHashSet<>(4);

    protected String bundlePrefix;

    protected Locale locale;

    @Override
    public Locale getLocale() { return locale; }

    public SELF setLocale(Locale locale) {
        this.locale = locale == null ? Locale.getDefault() : locale;
        return self();
    }

    public String getBundlePrefix() { return bundlePrefix; }

    public SELF setBundlePrefix(String bundlePrefix) {
        bundlePrefix =
            bundlePrefix == null ? getProperty(L10N_BASEDIR_KEY, L10N_BASEDIR_DEF).trim() : bundlePrefix.trim();
        if (bundlePrefix.length() > 0 && !bundlePrefix.endsWith("/")) {
            bundlePrefix += "/";
        }

        this.bundlePrefix = bundlePrefix;
        return self();
    }

    @NonNull
    public Set<String> getBundleBaseNames() { return unmodifiableSet(bundleBaseNames); }

    public SELF setBundleBaseNames(Collection<String> bundleBaseNames) {
        this.bundleBaseNames.clear();
        if (bundleBaseNames != null && !bundleBaseNames.isEmpty()) {
            this.bundleBaseNames.addAll(bundleBaseNames);
        }
        return self();
    }

    public SELF addBundleBaseNames(String... bundleBaseNames) {
        if (bundleBaseNames != null && bundleBaseNames.length > 0) {
            addAll(this.bundleBaseNames, bundleBaseNames);
        }
        return self();
    }

    @Override
    public String t(Locale locale, String key, Object... args) {
        if (key == null) {
            log.warn("Cannot translate text resource for a null key");
            return "";
        }

        if (locale == null) {
            locale = getLocale();
            if (locale == null) {
                locale = getDefaultLocale();
            }
        }

        try {

            final String message = message(key, locale).orElse(key);
            return args == null || args.length == 0
                   ? message
                   : messageFormat(message, locale, key).map(fmt -> fmt.format(args)).orElse(message);

        } catch (ClassCastException | MissingResourceException ex) {

            log.warn("Cannot translate text resource for key " + key, ex);
            return key;

        }
    }

    protected Optional<String> message(String key, Locale locale) {
        if (key == null) {
            return empty();
        }

        for (String bundleBaseName : bundleBaseNames) {
            if (bundleBaseName == null) {
                continue;
            }

            bundleBaseName = bundlePrefix + bundleBaseName;
            try {
                final ResourceBundle bundle = locale == null
                                              ? ResourceBundle.getBundle(bundleBaseName)
                                              : ResourceBundle.getBundle(bundleBaseName, locale);
                if (bundle != null) {
                    String message = bundle.getString(key);
                }
            } catch (MissingResourceException ex) {
                continue;
            }
        }

        return empty();
    }

    protected Optional<MessageFormat> messageFormat(String message, Locale locale, String key) {
        return of(new MessageFormat(message, locale));
    }

    /**
     * @see <a href="https://stackoverflow.com/a/23895571/272939">Answer to <em>Fluent API with inheritance and
     *     generics</em></a>
     * @see <a href="https://stackoverflow.com/a/7355094/272939">Is there a way to refer to the current type with a type
     */
    @SuppressWarnings("unchecked")
    protected final SELF self() {
        return (SELF) this;
    }
}
