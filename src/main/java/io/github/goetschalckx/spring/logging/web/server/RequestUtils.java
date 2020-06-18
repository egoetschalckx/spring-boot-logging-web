package io.github.goetschalckx.spring.logging.web.server;

import io.github.goetschalckx.spring.logging.web.LoggingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

public class RequestUtils {

    private static Logger log = LoggerFactory.getLogger(RequestUtils.class);

    public static String getRequestBody(HttpServletRequest request, int maxPayloadLength) {
        if (request == null) {
            return null;
        }

        String requestBody = null;

        // [eg] making a big guess here - will it be a MultiReadHttpServletRequestWrapper? NOBODY KNOWS...
        MultiReadHttpServletRequestWrapper requestWrapper = WebUtils.getNativeRequest(request, MultiReadHttpServletRequestWrapper.class);
        if (requestWrapper != null) {
            try {
                byte[] buf = requestWrapper.getRequestBody();

                if (buf.length > 0) {
                    int length = Math.min(buf.length, maxPayloadLength);
                    String payload;
                    try {
                        payload = new String(buf, 0, length, requestWrapper.getCharacterEncoding());
                    } catch (UnsupportedEncodingException ex) {
                        payload = LoggingConstants.UNKNOWN;
                    }

                    requestBody = payload;
                }
            } catch (IOException e) {
                log.error("IOException during getRequestBody()", e);
            }
        }

        return requestBody;
    }

    public static HttpHeaders getRequestHeaders(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Enumeration<String> names = request.getHeaderNames();
        HttpHeaders headers = new HttpHeaders();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                String value = values.nextElement();
                headers.add(name, value);
            }
        }

        return headers;
    }

}
