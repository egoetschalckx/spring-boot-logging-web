package io.github.goetschalckx.spring.logging.web.server;

import io.github.goetschalckx.spring.logging.web.LogArgUtils;
import io.github.goetschalckx.spring.logging.web.LogEventContext;
import io.github.goetschalckx.spring.logging.web.LoggingConstants;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import static io.github.goetschalckx.spring.logging.web.server.RequestUtils.getRequestBody;
import static io.github.goetschalckx.spring.logging.web.server.RequestUtils.getRequestHeaders;
import static io.github.goetschalckx.spring.logging.web.server.ResponseUtils.getResponseBody;
import static io.github.goetschalckx.spring.logging.web.server.ResponseUtils.getResponseHeaders;
import static io.github.goetschalckx.spring.logging.web.server.ResponseUtils.getResponseWrapper;

public class ServerLogger {

    private static Logger log = LoggerFactory.getLogger(ServerLogger.class);
    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 1024;

    public void logResponse(LogEventContext context, HttpServletResponse response) {
        int status = response.getStatus();
        context.getArgs().put(LoggingConstants.HTTP_RESPONSE_CODE, Integer.toString(status));

        if (context.getIncludeBody()) {
            String responseBody = getResponseBody(response);

            // TODO: at some point i had to (safely) read the stream before the headers so i wouldnt invalidate the stream... maybe double check this...
            HttpHeaders httpHeaders = getResponseHeaders(response);
            LogArgUtils.addHeaders(context.getArgs(), httpHeaders);

            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Outbound Message\n"
                            + "--------------------------------------\n"
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
                    status,
                    httpHeaders,
                    responseBody);
        } else {
            HttpServletResponseWrapper responseWrapper = getResponseWrapper(response);

            // TODO: at some point i had to (safely) read the stream before the headers so i wouldnt invalidate the stream... maybe double check this...
            HttpHeaders httpHeaders = getResponseHeaders(responseWrapper);
            LogArgUtils.addHeaders(context.getArgs(), httpHeaders);

            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Outbound Message\n"
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
                    responseWrapper.getStatus(),
                    httpHeaders);
        }
    }

    public void logRequest(LogEventContext context, HttpServletRequest request) {
        if (context.getIncludeBody()) {
            String requestBody = getRequestBody(request, DEFAULT_MAX_PAYLOAD_LENGTH);

            // TODO: like above. problem is i only want to read the stream if the thingy is enabled, but i always want the headers
            HttpHeaders httpHeaders = getRequestHeaders(request);
            LogArgUtils.addHeaders(context.getArgs(), httpHeaders);
            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Inbound Message\n"
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
                    httpHeaders,
                    requestBody);
        } else {
            HttpHeaders httpHeaders = getRequestHeaders(request);
            LogArgUtils.addHeaders(context.getArgs(), httpHeaders);

            log.info(
                    Markers.appendEntries(context.getArgs()),
                    "Inbound Message\n"
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
                    httpHeaders);
        }
    }

}
