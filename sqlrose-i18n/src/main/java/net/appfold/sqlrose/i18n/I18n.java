package net.appfold.sqlrose.i18n;

import java.text.MessageFormat;
import java.util.*;

import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Entry point/façade for a basic internationalization subsystem.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 2.0, May 17, 2019
 */
public class I18n {

    protected String bundlePrefix;

    protected Locale currentLocale;

    protected boolean cacheFormats = true;

    protected final Set<String> bundleBaseNames = new LinkedHashSet<>(4);

    public I18n() { this(null, null); }

    public I18n(String bundlePrefix) { this(null, bundlePrefix); }

    public I18n(Locale currentLocale) { this(currentLocale, null); }

    public I18n(Locale currentLocale, String bundlePrefix) {
        this.currentLocale = currentLocale == null ? Locale.getDefault() : currentLocale;

        bundlePrefix =
            bundlePrefix == null ? System.getProperty("sqlrose.l10n.prefix", "locales/").trim() : bundlePrefix.trim();
        if (bundlePrefix.length() > 0 && !bundlePrefix.endsWith("/")) {
            bundlePrefix += "/";
        }

        this.bundlePrefix = bundlePrefix;
    }

    public Locale getCurrentLocale() { return currentLocale; }

    public I18n setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale == null ? Locale.getDefault() : currentLocale;
        return this;
    }

    public String getBundlePrefix() { return bundlePrefix; }

    public I18n setBundlePrefix(String bundlePrefix) {
        bundlePrefix =
            bundlePrefix == null ? System.getProperty("sqlrose.l10n.prefix", "locales/").trim() : bundlePrefix.trim();
        if (bundlePrefix.length() > 0 && !bundlePrefix.endsWith("/")) {
            bundlePrefix += "/";
        }

        this.bundlePrefix = bundlePrefix;
        return this;
    }

    public String t(String key, Object... args) { return t(null, key, args); }

    public String t(Locale locale, String key, Object... args) {
        if (key == null) {
            return "";
        }

        if (locale == null) {
            locale = getCurrentLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        }

        try {

            final String message = ResourceBundle.getBundle(bundlePrefix + "Messages", locale).getString(key);
            return args == null || args.length == 0
                   ? message
                   : messageFormat(message, locale, key).map(fmt -> fmt.format(args)).orElse(message);

        } catch (ClassCastException | MissingResourceException ex) {

            getLogger(getClass()).warn("Cannot translate text resource for key " + key, ex);
            return key;

        }
    }

    protected Optional<MessageFormat> messageFormat(String message, Locale locale, String key) {
        return of(new MessageFormat(message, locale));
    }
}
