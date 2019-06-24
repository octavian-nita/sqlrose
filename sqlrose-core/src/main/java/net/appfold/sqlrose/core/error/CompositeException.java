package net.appfold.sqlrose.core.error;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static net.appfold.sqlrose.core.error.ErrorCode.E_COMPOSITE;

/**
 * An {@link SqlRoseException exception} whose purpose is to {@link #add(Throwable...) accumulate} one or more other
 * exceptions (to be handled at a later point, for example). {@code null} arguments are generally ignored, and only the
 * {@link #getExceptions() content} of other {@link CompositeException composite exceptions} is accumulated in order to
 * avoid recursive structures. In other words, a {@link CompositeException} <strong>cannot</strong> contain other
 * {@link CompositeException}s.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 13, 2019
 */
public final class CompositeException extends SqlRoseException implements Iterable<Throwable> {

    private final List<Throwable> exceptions = new ArrayList<>();

    public CompositeException(Throwable... exceptions) {
        this(exceptions == null ? null : asList(exceptions));
    }

    public CompositeException(Iterable<? extends Throwable> exceptions) {
        super(E_COMPOSITE);
        add(exceptions);
    }

    public CompositeException add(Throwable... exceptions) {
        return add(exceptions == null ? null : asList(exceptions));
    }

    public CompositeException add(Iterable<? extends Throwable> exceptions) {
        if (exceptions != null) {
            for (Throwable exception : exceptions) {
                if (exception != null && !exception.equals(this)) {
                    if (exception instanceof CompositeException) {
                        this.exceptions.addAll(((CompositeException) exception).exceptions);
                    } else {
                        this.exceptions.add(exception);
                    }
                }
            }
        }
        return this;
    }

    public final List<Throwable> getExceptions() { return unmodifiableList(exceptions); }

    @Override
    public final Iterator<Throwable> iterator() { return exceptions.iterator(); }

    public final int size() { return exceptions.size(); }

    /**
     * @return an array containing exactly one, non-{@code null} {@link Integer}, the value returned by {@link #size()}
     */
    @Override
    public final Object[] getDetails() {
        return new Integer[]{size()};
    }
}
