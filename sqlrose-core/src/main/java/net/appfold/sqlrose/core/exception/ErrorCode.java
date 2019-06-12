package net.appfold.sqlrose.core.exception;

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
