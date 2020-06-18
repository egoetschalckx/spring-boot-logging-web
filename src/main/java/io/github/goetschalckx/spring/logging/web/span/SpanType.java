package io.github.goetschalckx.spring.logging.web.span;

import java.util.Locale;

public enum SpanType {
    CLIENT,
    SERVER;

    SpanType() {
    }

    public String logValue() {
        return this.name().toLowerCase(Locale.getDefault());
    }
}
