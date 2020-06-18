package io.github.goetschalckx.spring.logging.web.client;

import io.github.goetschalckx.spring.logging.web.LogArgUtils;
import io.github.goetschalckx.spring.logging.web.LogEventContext;
import io.github.goetschalckx.spring.logging.web.LoggingConstants;

import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientLogger {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClientLogger.class);

    public void logResponse(LogEventContext context, ClientHttpResponse response) {
        String statusCode = ClientLoggingUtils.getStatusCode(response);
        LogArgUtils.addIfValuePresent(context.getArgs(), LoggingConstants.HTTP_RESPONSE_CODE, statusCode);
        LogArgUtils.addHeaders(context.getArgs(), response.getHeaders());

        if (context.getIncludeBody()) {
            String responseBody;
            try {
                responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("IOException during copyToString");
                responseBody = LoggingConstants.UNKNOWN;
            }

            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Inbound Message\n"
                            + "----------------------------\n"
                            + "Span Kind: {}\n"
                            + "Span ID: {}\n"
                            + "Method: {}\n"
                            + "URL: {}\n"
                            + "Status Code: {}\n"
                            + "Headers: {}\n"
                            + "Payload: {}\n"
                            + "--------------------------------------",
                    context.getSpanKind(),
                    context.getSpanId(),
                    context.getMethod(),
                    context.getUrl(),
                    statusCode,
                    response.getHeaders(),
                    responseBody);
        } else {
            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Inbound Message\n"
                            + "--------------------------------------\n"
                            + "Span Kind: {}\n"
                            + "Span ID: {}\n"
                            + "Method: {}\n"
                            + "URL: {}\n"
                            + "Status Code: {}\n"
                            + "Headers: {}\n"
                            + "--------------------------------------",
                    context.getSpanKind(),
                    context.getSpanId(),
                    context.getMethod(),
                    context.getUrl(),
                    statusCode,
                    response.getHeaders());
        }
    }

    public void logRequest(LogEventContext context, HttpRequest request, byte[] body) {
        LogArgUtils.addHeaders(context.getArgs(), request.getHeaders());

        if (context.getIncludeBody()) {
            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Outbound Message\n"
                            + "--------------------------------------\n"
                            + "Span Kind: {}\n"
                            + "Span ID: {}\n"
                            + "Method: {}\n"
                            + "URL: {}\n"
                            + "Headers: {}\n"
                            + "Payload: {}\n"
                            + "--------------------------------------",
                    context.getSpanKind(),
                    context.getSpanId(),
                    context.getMethod(),
                    context.getUrl(),
                    request.getHeaders(),
                    new String(body, StandardCharsets.UTF_8));
        } else {
            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Outbound Message\n"
                            + "--------------------------------------\n"
                            + "Span Kind: {}\n"
                            + "Span ID: {}\n"
                            + "Method: {}\n"
                            + "URL: {}\n"
                            + "Headers: {}\n"
                            + "--------------------------------------",
                    context.getSpanKind(),
                    context.getSpanId(),
                    context.getMethod(),
                    context.getUrl(),
                    request.getHeaders());
        }
    }

}
