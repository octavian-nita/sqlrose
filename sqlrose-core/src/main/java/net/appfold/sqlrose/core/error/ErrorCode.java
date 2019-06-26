package net.appfold.sqlrose.core.error;

import lombok.*;

import java.io.Serializable;
import java.util.concurrent.*;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 12, 2019
 */
@Getter
@EqualsAndHashCode
public final class ErrorCode implements Comparable<ErrorCode>, Serializable {

    private static final ConcurrentMap<String, ErrorCode> valueCache = new ConcurrentHashMap<>();

    public static final ErrorCode E_GENERIC = errorCode("E_GENERIC");

    public static final ErrorCode E_COMPOSITE = errorCode("E_COMPOSITE");

    public static final ErrorCode E_NO_DETAILS = errorCode("E_NO_DETAILS");

    @NonNull
    public static ErrorCode errorCode(@NonNull CharSequence value) {
        return valueCache.computeIfAbsent(value.toString(), ErrorCode::new);
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

    @NonNull
    public String value() {
        return getValue();
    }

    public boolean is(CharSequence value) {
        return value != null && this.value.equals(value.toString());
    }
}
