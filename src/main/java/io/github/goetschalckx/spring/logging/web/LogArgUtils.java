package io.github.goetschalckx.spring.logging.web;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Map;

public class LogArgUtils {

    public static void addIfValuePresent(Map<String, String> args, String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }

        args.put(key, value);
    }

    public static void addHeaders(Map<String, String> args, HttpHeaders httpHeaders) {
        Locale defaultLocale = Locale.getDefault();

        httpHeaders.forEach(
                (key, value) ->
                        addIfValuePresent(
                                args,
                                LoggingConstants.HTTP_HEADER_PREFIX + key.toLowerCase(defaultLocale),
                                value.get(0)));
    }

}
