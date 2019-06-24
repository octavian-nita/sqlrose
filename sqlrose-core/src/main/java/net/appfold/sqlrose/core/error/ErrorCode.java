package net.appfold.sqlrose.core.error;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 12, 2019
 */
@Getter
@EqualsAndHashCode
public final class ErrorCode implements Comparable<ErrorCode>, Serializable {

    public static final ErrorCode E_GENERIC = new ErrorCode("E_GENERIC");

    public static final ErrorCode E_COMPOSITE = new ErrorCode("E_COMPOSITE");

    public static final ErrorCode E_NO_DETAILS = new ErrorCode("E_NO_DETAILS");

    private static final Map<String, ErrorCode> valueCache = new ConcurrentHashMap<>();

    public static ErrorCode errorCode(CharSequence value) {
        return valueCache.computeIfAbsent(value == null ? null : value.toString(), ErrorCode::new);
    }

    private final String value;

    private ErrorCode(String value) {
        if (value != null) {
            value = value.trim();
        }
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException("An error code value cannot be blank");
        }
        this.value = value;
    }

    /**
     * Naturally, lexicographically ordered by {@link #getValue() value}.
     */
    @Override
    public int compareTo(ErrorCode errorCode) {
        return value.compareTo(errorCode.value);
    }

    @Override
    public String toString() {
        return getValue();
    }

    public String value() {
        return getValue();
    }

    public boolean is(CharSequence value) {
        return value != null && this.value.equals(value.toString());
    }
}
