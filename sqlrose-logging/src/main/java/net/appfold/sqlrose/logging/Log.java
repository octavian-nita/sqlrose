package net.appfold.sqlrose.logging;

import me.nallar.whocalled.WhoCalled;
import org.slf4j.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Entry point for various <a href="https://www.slf4j.org/">SLF4J</a>-based, logging-related utilities.
 * <p/>
 * The various methods using format strings pass them and the rest of the arguments directly to SLF4J.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 2.0, Nov 8, 2017
 */
public interface Log {

    /**
     * Performs additional logging initialization, like {@link #routeJulToSlf4j() adapting j.u.l to log}, etc.
     * <p/>
     * Should probably be invoked early during the initialization phase of the application.
     */
    static void init() {
        routeJulToSlf4j();
    }

    /**
     * Route any j.u.l logging request to SLF4J.
     *
     * @see <a href="http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html">Bridging
     * java.util.logging to SLF4J</a>
     * @see <a href="http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge">JUL to SLF4J Bridge</a>
     * @see <a href="https://logback.qos.ch/manual/configuration.html#LevelChangePropagator">LevelChangePropagator
     * configuration</a>
     */
    static void routeJulToSlf4j() {
        // We might not need this if we're installing a LevelChangePropagator
        java.util.logging.LogManager.getLogManager().reset();

        SLF4JBridgeHandler.removeHandlersForRootLogger(); // avoid having everything logged twice
        SLF4JBridgeHandler.install();

        java.util.logging.Logger.getGlobal().setLevel(java.util.logging.Level.FINE /* -> DEBUG in SLF4J */);
    }

    default void d(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(source);
        slf4j.debug("", throwable);
        slf4j.debug(fmt, args);
    }

    default void d(final Object source, final String msg, final Throwable throwable) {
        log(source).debug(msg, throwable);
    }

    default void d(final Object source, final String fmt, final Object... args) {
        log(source).debug(fmt, args);
    }

    default void d(final Object source, final Throwable throwable) {
        log(source).debug("", throwable);
    }

    default void d(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(null);
        slf4j.debug("", throwable);
        slf4j.debug(fmt, args);
    }

    default void d(final String msg, final Throwable throwable) {
        log(null).debug(msg, throwable);
    }

    default void d(final String fmt, final Object... args) {
        log(null).debug(fmt, args);
    }

    default void d(final Throwable throwable) {
        log(null).debug("", throwable);
    }

    default void i(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(source);
        slf4j.info("", throwable);
        slf4j.info(fmt, args);
    }

    default void i(final Object source, final String msg, final Throwable throwable) {
        log(source).info(msg, throwable);
    }

    default void i(final Object source, final String fmt, final Object... args) {
        log(source).info(fmt, args);
    }

    default void i(final Object source, final Throwable throwable) {
        log(source).info("", throwable);
    }

    default void i(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(null);
        slf4j.info("", throwable);
        slf4j.info(fmt, args);
    }

    default void i(final String msg, final Throwable throwable) {
        log(null).info(msg, throwable);
    }

    default void i(final String fmt, final Object... args) {
        log(null).info(fmt, args);
    }

    default void i(final Throwable throwable) {
        log(null).info("", throwable);
    }

    default void w(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(source);
        slf4j.warn("", throwable);
        slf4j.warn(fmt, args);
    }

    default void w(final Object source, final String msg, final Throwable throwable) {
        log(source).warn(msg, throwable);
    }

    default void w(final Object source, final String fmt, final Object... args) {
        log(source).warn(fmt, args);
    }

    default void w(final Object source, final Throwable throwable) {
        log(source).warn("", throwable);
    }

    default void w(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(null);
        slf4j.warn("", throwable);
        slf4j.warn(fmt, args);
    }

    default void w(final String msg, final Throwable throwable) {
        log(null).warn(msg, throwable);
    }

    default void w(final String fmt, final Object... args) {
        log(null).warn(fmt, args);
    }

    default void w(final Throwable throwable) {
        log(null).warn("", throwable);
    }

    default void e(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(source);
        slf4j.error("", throwable);
        slf4j.error(fmt, args);
    }

    default void e(final Object source, final String msg, final Throwable throwable) {
        log(source).error(msg, throwable);
    }

    default void e(final Object source, final String fmt, final Object... args) {
        log(source).error(fmt, args);
    }

    default void e(final Object source, final Throwable throwable) {
        log(source).error("", throwable);
    }

    default void e(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = log(null);
        slf4j.error("", throwable);
        slf4j.error(fmt, args);
    }

    default void e(final String msg, final Throwable throwable) {
        log(null).error(msg, throwable);
    }

    default void e(final String fmt, final Object... args) {
        log(null).error(fmt, args);
    }

    default void e(final Throwable throwable) {
        log(null).error("", throwable);
    }

    default Logger log() {
        return log(null);
    }

    default Logger log(final Object source) {
        if (source == null) {
            return LoggerFactory.getLogger(WhoCalled.$.getCallingClass());
        } else if (source instanceof String) {
            return LoggerFactory.getLogger((String) source);
        } else if (source instanceof Class<?>) {
            return LoggerFactory.getLogger((Class<?>) source);
        } else {
            return LoggerFactory.getLogger(source.getClass());
        }
    }
}
