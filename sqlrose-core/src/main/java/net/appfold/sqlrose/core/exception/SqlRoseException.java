package net.appfold.sqlrose.core.exception;

import static java.util.Arrays.copyOf;

/**
 * Base class for many SQLrose-specific exceptions. Allows specifying an {@link ErrorCode error code} and additional
 * {@link #getDetails() information} which could be used, for example, to resolve locale-specific error messages or
 * to choose between different courses of action when handling the exception.
 * <p>
 * N.B. While there are <a href="https://www.ibm.com/developerworks/library/j-jtp05254/">good reasons</a> to use {@link
 * Exception checked exceptions}, some APIs and (event-based) frameworks might not offer an easy way to propagate them
 * (e.g., out of handlers) and it is usually not appropriate to handle exceptions at the place of their occurrence. As
 * such, one might consider using a hierarchy of unchecked exceptions and relying on a global / default error handler.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 12, 2019
 */
public class SqlRoseException extends RuntimeException {

    private final ErrorCode errorCode;

    private final Object[] details;

    public SqlRoseException(String message) {
        super(message);
        this.errorCode = null;
        this.details = null;
    }

    public SqlRoseException(Throwable cause) {
        super(cause);
        this.errorCode = null;
        this.details = null;
    }

    public SqlRoseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.details = null;
    }

    public SqlRoseException(ErrorCode errorCode, Object... details) {
        this.errorCode = errorCode;
        this.details = details == null ? null : copyOf(details, details.length);
    }

    public SqlRoseException(Throwable cause, ErrorCode errorCode, Object... details) {
        super(cause);
        this.errorCode = errorCode;
        this.details = details == null ? null : copyOf(details, details.length);
    }

    public ErrorCode getErrorCode() { return errorCode; }

    public String getCode() { return errorCode == null ? null : errorCode.value(); }

    public Object[] getDetails() { return details == null ? null : copyOf(details, details.length); }
}
