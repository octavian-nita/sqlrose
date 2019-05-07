package net.appfold.sqlrose.logging;

import org.slf4j.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 2.0, Nov 8, 2017
 */
public class Log {

    static {
        init();
    }

    /**
     * Performs additional initialization, like adapting j.u.l to the logging
     * framework of choice, etc.
     */
    public static void init() {
        routeJulToSlf4j();
    }

    /**
     * Route any j.u.l logging request to SLF4J.
     *
     * @see <a href=
     *     "http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html">
     *     http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html</a>
     * @see <a href=
     *     "http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge">
     *     http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge</a>
     * @see <a href=
     *     "https://logback.qos.ch/manual/configuration.html#LevelChangePropagator">
     *     https://logback.qos.ch/manual/configuration.html#LevelChangePropagator</a>
     */
    private static void routeJulToSlf4j() {
        // We might not need this if we're installing a LevelChangePropagator
        java.util.logging.LogManager.getLogManager().reset();

        SLF4JBridgeHandler.removeHandlersForRootLogger(); // avoid having everything logged twice
        SLF4JBridgeHandler.install();

        java.util.logging.Logger.getGlobal().setLevel(java.util.logging.Level.CONFIG /* -> INFO in SLF4J API */);
    }

    public static void d(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(source);
        slf4j.debug("", throwable);
        slf4j.debug(fmt, args);
    }

    public static void d(final Object source, final String msg, final Throwable throwable) {
        slf4j(source).debug(msg, throwable);
    }

    public static void d(final Object source, final String fmt, final Object... args) {
        slf4j(source).debug(fmt, args);
    }

    public static void d(final Object source, final Throwable throwable) {
        slf4j(source).debug("", throwable);
    }

    public static void d(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(null);
        slf4j.debug("", throwable);
        slf4j.debug(fmt, args);
    }

    public static void d(final String msg, final Throwable throwable) {
        slf4j(null).debug(msg, throwable);
    }

    public static void d(final String fmt, final Object... args) {
        slf4j(null).debug(fmt, args);
    }

    public static void d(final Throwable throwable) {
        slf4j(null).debug("", throwable);
    }

    public static void i(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(source);
        slf4j.info("", throwable);
        slf4j.info(fmt, args);
    }

    public static void i(final Object source, final String msg, final Throwable throwable) {
        slf4j(source).info(msg, throwable);
    }

    public static void i(final Object source, final String fmt, final Object... args) {
        slf4j(source).info(fmt, args);
    }

    public static void i(final Object source, final Throwable throwable) {
        slf4j(source).info("", throwable);
    }

    public static void i(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(null);
        slf4j.info("", throwable);
        slf4j.info(fmt, args);
    }

    public static void i(final String msg, final Throwable throwable) {
        slf4j(null).info(msg, throwable);
    }

    public static void i(final String fmt, final Object... args) {
        slf4j(null).info(fmt, args);
    }

    public static void i(final Throwable throwable) {
        slf4j(null).info("", throwable);
    }

    public static void w(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(source);
        slf4j.warn("", throwable);
        slf4j.warn(fmt, args);
    }

    public static void w(final Object source, final String msg, final Throwable throwable) {
        slf4j(source).warn(msg, throwable);
    }

    public static void w(final Object source, final String fmt, final Object... args) {
        slf4j(source).warn(fmt, args);
    }

    public static void w(final Object source, final Throwable throwable) {
        slf4j(source).warn("", throwable);
    }

    public static void w(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(null);
        slf4j.warn("", throwable);
        slf4j.warn(fmt, args);
    }

    public static void w(final String msg, final Throwable throwable) {
        slf4j(null).warn(msg, throwable);
    }

    public static void w(final String fmt, final Object... args) {
        slf4j(null).warn(fmt, args);
    }

    public static void w(final Throwable throwable) {
        slf4j(null).warn("", throwable);
    }

    public static void e(final Object source, final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(source);
        slf4j.error("", throwable);
        slf4j.error(fmt, args);
    }

    public static void e(final Object source, final String msg, final Throwable throwable) {
        slf4j(source).error(msg, throwable);
    }

    public static void e(final Object source, final String fmt, final Object... args) {
        slf4j(source).error(fmt, args);
    }

    public static void e(final Object source, final Throwable throwable) {
        slf4j(source).error("", throwable);
    }

    public static void e(final Throwable throwable, final String fmt, final Object... args) {
        final Logger slf4j = slf4j(null);
        slf4j.error("", throwable);
        slf4j.error(fmt, args);
    }

    public static void e(final String msg, final Throwable throwable) {
        slf4j(null).error(msg, throwable);
    }

    public static void e(final String fmt, final Object... args) {
        slf4j(null).error(fmt, args);
    }

    public static void e(final Throwable throwable) {
        slf4j(null).error("", throwable);
    }

    private static Logger slf4j(final Object source) {
        if (source == null) {
            return LoggerFactory.getLogger(whoCalled.get());
        } else if (source instanceof String) {
            return LoggerFactory.getLogger((String) source);
        } else if (source instanceof Class<?>) {
            return LoggerFactory.getLogger((Class<?>) source);
        } else {
            return LoggerFactory.getLogger(source.getClass());
        }
    }

    private static final WhoCalled whoCalled = new WhoCalled();

    /**
     * @see <a href="https://github.com/nallar/WhoCalled">Kudos to Ross Allan!</a>
     */
    private static final class WhoCalled extends SecurityManager {

        private Class<?> get() {
            final Class<?>[] ctx = getClassContext();
            return ctx.length > 3 ? ctx[3] : ctx[ctx.length - 1];
        }
    }
}
