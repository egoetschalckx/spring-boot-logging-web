package io.github.goetschalckx.spring.logging.web.span;

import java.util.UUID;

public class SpanIdGenerator {

    public String spanId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
