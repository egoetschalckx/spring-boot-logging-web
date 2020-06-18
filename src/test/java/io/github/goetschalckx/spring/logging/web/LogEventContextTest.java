package io.github.goetschalckx.spring.logging.web;

import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LogEventContextTest {

    private boolean includeBody = true;
    private String spanKind = "producer";
    private String spanId = "42";
    private String method = "PATCH";
    private String url = "https://example.com";

    private LogEventContext logEventContext = new LogEventContext(
            includeBody,
            spanKind,
            spanId,
            method,
            url
    );

    @Test
    public void testGetIncludeBody() {
        assertThat(logEventContext.getIncludeBody(), is(includeBody));
    }

    @Test
    public void testGetSpanKind() {
        assertThat(logEventContext.getSpanKind(), is(spanKind));
    }

    @Test
    public void testGetSpanId() {
        assertThat(logEventContext.getSpanId(), is(spanId));
    }

    @Test
    public void testGetMethod() {
        assertThat(logEventContext.getMethod(), is(method));
    }

    @Test
    public void testGetGetUrl() {
        assertThat(logEventContext.getUrl(), is(url));
    }

    @Test
    public void testDefaultArgs() {
        Map<String, String> args = logEventContext.getArgs();
        assertThat(args.get(LoggingConstants.SPAN_KIND), is(spanKind));
        assertThat(args.get(LoggingConstants.SPAN_ID), is(spanId));
        assertThat(args.get(LoggingConstants.HTTP_METHOD), is(method));
        assertThat(args.get(LoggingConstants.HTTP_URL), is(url));
    }

    @Test
    public void testAddArgIfPresent() {
        String key = UUID.randomUUID().toString();
        String val = UUID.randomUUID().toString();

        logEventContext.addArgIfPresent(key, val);

        assertThat(logEventContext.getArgs().get(key), is(val));
    }

    @Test
    public void testAddArgIfPresentWithEmpty() {
        int argsSize = logEventContext.getArgs().size();
        String val = UUID.randomUUID().toString();

        logEventContext.addArgIfPresent("", val);

        assertThat(logEventContext.getArgs().size(), is(argsSize));
    }

    @Test
    public void testAddArgIfPresentWithNull() {
        int argsSize = logEventContext.getArgs().size();
        String val = UUID.randomUUID().toString();

        logEventContext.addArgIfPresent(null, val);

        assertThat(logEventContext.getArgs().size(), is(argsSize));
    }

}
