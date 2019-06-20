package net.appfold.sqlrose.core.exception;

import lombok.NonNull;
import net.appfold.sqlrose.i18n.I18n;
import org.slf4j.Logger;

import static java.lang.Thread.currentThread;
import static net.appfold.sqlrose.core.exception.ErrorCode.E_NO_DETAILS;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 19, 2019
 */
public class SimpleExceptionHandler<SELF extends SimpleExceptionHandler<SELF>>
    implements ExceptionHandler<Throwable>, Thread.UncaughtExceptionHandler {

    @NonNull
    protected I18n i18n;

    @NonNull
    protected Logger log;

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
        if (exception == null) {
            log.error(i18n.t(E_NO_DETAILS.value()));
            return;
        }

        if (exception instanceof CompositeException) {

        } else if (exception instanceof SqlRoseException) {

        } else {

        }
    }
}
