package net.appfold.sqlrose.core.error;

import static java.util.Arrays.asList;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 21, 2019
 */
public interface ErrorReport {

    void report();

    void addGenericReport();

    void add(Iterable<? extends Throwable> exceptions);

    default void add(Throwable... exceptions) { add(exceptions == null ? null : asList(exceptions)); }
}
