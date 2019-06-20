package net.appfold.sqlrose.core.exception;

import java.util.function.Consumer;

/**
 * A generic strategy for handling errors, similar in shape (and intent) to Spring's <a
 * href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/util/ErrorHandler.html">ErrorHandler</a>.
 *
 * @param <T> the exact type of {@link Throwable exception} to handle
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 19, 2019
 */
@FunctionalInterface
public interface ExceptionHandler<T extends Throwable> extends Consumer<T> {

    void handle(T exception);

    @Override
    default void accept(T exception) {
        handle(exception);
    }
}
