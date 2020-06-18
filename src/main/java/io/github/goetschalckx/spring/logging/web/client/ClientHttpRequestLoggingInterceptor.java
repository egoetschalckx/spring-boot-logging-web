package io.github.goetschalckx.spring.logging.web.client;

import io.github.goetschalckx.spring.logging.web.LogEventContext;
import io.github.goetschalckx.spring.logging.web.LoggingConstants;
import io.github.goetschalckx.spring.logging.web.span.SpanType;
import io.github.goetschalckx.spring.logging.web.span.SpanIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ClientHttpRequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ClientHttpRequestLoggingInterceptor.class);
    private static final String SPAN_KIND_CLIENT = SpanType.CLIENT.logValue();

    private final boolean includeBody;
    private final ClientLogger clientLogger;
    private final SpanIdGenerator spanIdGenerator;

    public ClientHttpRequestLoggingInterceptor(
            boolean includeBody,
            ClientLogger clientLogger,
            SpanIdGenerator spanIdGenerator
    ) {
        this.includeBody = includeBody;
        this.clientLogger = clientLogger;
        this.spanIdGenerator = spanIdGenerator;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {

        // TODO: [eg] i really hate these... is there a better way? what if its not info level?
        if (!log.isInfoEnabled()) {
            return execution.execute(request, body);
        }

        String spanId = spanIdGenerator.spanId();
        MDC.put(LoggingConstants.SPAN_ID, spanId);
        MDC.put(LoggingConstants.SPAN_KIND, SPAN_KIND_CLIENT);

        try {
            return log(request, body, execution, spanId);
        } finally {
            MDC.remove(LoggingConstants.SPAN_ID);
            MDC.remove(LoggingConstants.SPAN_KIND);
        }
    }

    private ClientHttpResponse log(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution,
            String spanId
    ) throws IOException {
        String method = request.getMethod().name();
        String url = request.getURI().toString();

        LogEventContext context = new LogEventContext(
                includeBody,
                SPAN_KIND_CLIENT,
                spanId,
                method,
                url);

        clientLogger.logRequest(context, request, body);

        final ClientHttpResponse response = execution.execute(request, body);

        // i wonder if we might have the same shared memory problem here as in server logging
        clientLogger.logResponse(context, response);

        return response;
    }

}
