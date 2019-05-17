package net.appfold.sqlrose.i18n;

import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

/**
 * Entry point/façade for a basic internationalization subsystem.
 *
 * @author Octavian Nita (https://github.com/octavian-nita/)
 * @version 2.0, May 17, 2019
 */
public class I18n {

    protected Locale currentLocale;

    protected String bundleBaseName;

    protected String bundlePrefix;

    public I18n() { this(null, null); }

    public I18n(String bundlePrefix) {
        this(bundlePrefix, null);
    }

    public I18n(String bundlePrefix, Locale currentLocale) {
        bundlePrefix =
            bundlePrefix == null ? System.getProperty("sqlrose.l10n.prefix", "locales/").trim() : bundlePrefix.trim();
        if (bundlePrefix.length() > 0 && !bundlePrefix.endsWith("/")) {
            bundlePrefix += "/";
        }

        this.bundlePrefix = bundlePrefix;
        this.currentLocale = currentLocale == null ? Locale.getDefault() : currentLocale;
    }

    public Locale getCurrentLocale() { return currentLocale; }

    public I18n setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale == null ? Locale.getDefault() : currentLocale;
        return this;
    }

    public String t(String key, Object... args) { return t(key, null, args); }

    public String t(String key, Locale locale, Object... args) {
        if (key == null) {
            return "!?";
        }

        if (locale == null) {
            locale = getCurrentLocale();
            if (locale == null) {
                return "!" + key;
            }
        }

        try {
            final ResourceBundle bundle = ResourceBundle.getBundle(bundlePrefix + "Messages", locale);

            return args == null || args.length == 0
                   ? bundle.getString(key)
                   : new MessageFormat(bundle.getString(key), locale).format(args);

        } catch (ClassCastException | MissingResourceException ex) {
            LoggerFactory.getLogger(getClass()).warn("Cannot translate text resource for key " + key, ex);
            return "!" + key;
        }
    }
}
