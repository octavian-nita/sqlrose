package net.appfold.sqlrose.i18n;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.BiFunction;

import static java.lang.System.getProperty;
import static java.util.Collections.addAll;
import static java.util.ResourceBundle.getBundle;
import static net.appfold.sqlrose.cache.MemoizedBiFn.memoize;

/**
 * A fairly <em>simple</em> implementation of {@link I18n} using the <em><a
 * href="https://en.wikipedia.org/wiki/Curiously_recurring_template_pattern">curiously recurring template pattern
 * </a></em>.
 * <p/>
 * Very similar in behaviour to <a href="https://spring.io/projects/spring-framework">Spring Framework's</a>
 * <code><a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ResourceBundleMessageSource.html">
 * ResourceBundleMessageSource</a></code>, this class relies on the underlying JDK's {@link ResourceBundle}
 * implementation in combination with the JDK's standard message parsing provided by {@link MessageFormat},
 * caches by default the generated message formats for each message and can be configured concerning how both the
 * {@link #setResourceBundleSupplier(BiFunction) resource bundles} and the {@link #setMessageFormatSupplier(BiFunction)
 * message formats} are created. It also suffers from the same shortcomings as {@code ResourceBundleMessageSource} so a
 * {@code ReloadableResourceBundleMessageSource} might be a better choice if the client application is based on Spring.
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

    /** Can be {@code null} */
    protected String bundlePrefix;

    /** Can be {@code null} */
    protected Locale locale;

    /** Can be {@code null} */
    protected BiFunction<String, Locale, MessageFormat> messageFormatSupplier = memoize(MessageFormat::new);

    /** Can be {@code null} */
    protected BiFunction<String, Locale, ResourceBundle> resourceBundleSupplier =
        (baseName, locale) -> locale == null ? getBundle(baseName) : getBundle(baseName, locale);

    public SimpleI18n(String... bundleBaseNames) {
        setBundlePrefix(getProperty(L10N_BASEDIR_KEY, L10N_BASEDIR_DEF).trim());
        addBundleBaseNames(bundleBaseNames);
    }

    public SELF addBundleBaseNames(String... bundleBaseNames) {
        if (bundleBaseNames != null && bundleBaseNames.length > 0) {
            addAll(this.bundleBaseNames, bundleBaseNames);
        }
        return self();
    }

    public SELF setBundleBaseNames(Collection<String> bundleBaseNames) {
        this.bundleBaseNames.clear();
        if (bundleBaseNames != null && !bundleBaseNames.isEmpty()) {
            this.bundleBaseNames.addAll(bundleBaseNames);
        }
        return self();
    }

    public SELF setBundlePrefix(String bundlePrefix) {
        if (bundlePrefix != null) {
            bundlePrefix = bundlePrefix.trim();
            if (bundlePrefix.length() > 0 && !bundlePrefix.endsWith("/")) {
                bundlePrefix += "/";
            }
        }

        this.bundlePrefix = bundlePrefix;
        return self();
    }

    public SELF setMessageFormatSupplier(BiFunction<String, Locale, MessageFormat> messageFormatSupplier) {
        this.messageFormatSupplier = messageFormatSupplier;
        return self();
    }

    public SELF setResourceBundleSupplier(BiFunction<String, Locale, ResourceBundle> resourceBundleSupplier) {
        this.resourceBundleSupplier = resourceBundleSupplier;
        return self();
    }

    public SELF setLocale(Locale locale) {
        this.locale = locale == null ? Locale.getDefault() : locale;
        return self();
    }

    @Override
    public Locale getLocale() { return locale; }

    @NonNull
    @Override
    public String t(Locale locale, String key, Object... args) {
        if (key == null) {
            log.debug("Cannot translate resource for a null key");
            return "";
        }

        if (locale == null) {
            locale = getLocale();
            if (locale == null) {
                locale = getDefaultLocale();
            }
        }

        final String message = message(key, locale);
        if (args == null || args.length == 0 || messageFormatSupplier == null) {
            return message;
        }

        final MessageFormat format = messageFormatSupplier.apply(message, locale);
        return format == null ? message : format.format(args);
    }

    @NonNull
    protected String message(@NonNull String key, Locale locale) {
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
