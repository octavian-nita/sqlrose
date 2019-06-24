package net.appfold.sqlrose.core.error;

import lombok.NonNull;
import net.appfold.sqlrose.i18n.*;
import net.appfold.sqlrose.logging.Log;
import org.slf4j.Logger;

import java.util.function.Supplier;

import static java.lang.Thread.currentThread;
import static net.appfold.sqlrose.core.error.ErrorCode.E_NO_DETAILS;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 19, 2019
 */
public class SimpleExceptionHandler<SELF extends SimpleExceptionHandler<SELF>>
    implements ExceptionHandler<Throwable>, Thread.UncaughtExceptionHandler {

    @NonNull
    protected Logger log;

    protected I18n i18n;

    protected Supplier<ErrorReport> errorReportSupplier;

    public SimpleExceptionHandler(Logger log, I18n i18n) {
        this.log = log == null ? Log.log() : log;
        this.i18n = i18n == null ? new SimpleI18n<>("errors") : i18n;
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

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        log(thread);
        handle(exception);
    }

    protected void log(Thread thread) {
        if (thread != null && thread != currentThread()) { // log thread info only if handling in another thread...
            if (log.isDebugEnabled()) {
                final String groupName = thread.getThreadGroup() == null ? null : thread.getThreadGroup().getName();
                if (groupName == null) {
                    log.debug("[{}]", thread.getName());
                } else {
                    log.debug("[{}.{}]", groupName, thread.getName());
                }
            }
        }
    }

    @Override
    public void handle(Throwable exception) {
        try {

            if (exception == null) {
                log.error(i18n.t(E_NO_DETAILS));
                return;
            }

            final ErrorReport errorReport = errorReportSupplier == null ? null : errorReportSupplier.get();

            if (exception instanceof SqlRoseException) {

                final SqlRoseException sqlRoseEx = (SqlRoseException) exception;
                final ErrorCode errorCode = sqlRoseEx.getCode();

                if (sqlRoseEx instanceof CompositeException) {
                    final CompositeException compositeEx = (CompositeException) sqlRoseEx;
                }

            } else {
                log.error("", exception);
                if (errorReport != null) {
                    errorReport.add(exception);
                }
            }

            if (errorReport != null) {
                errorReport.report();
            }

        } catch (Throwable throwable) { // overly cautious?
            log.error("?", throwable);
        }
    }
}
