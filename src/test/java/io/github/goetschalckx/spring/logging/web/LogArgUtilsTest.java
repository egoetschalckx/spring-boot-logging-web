package io.github.goetschalckx.spring.logging.web;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static io.github.goetschalckx.spring.logging.web.LogArgUtils.addHeaders;
import static io.github.goetschalckx.spring.logging.web.LogArgUtils.addIfValuePresent;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LogArgUtilsTest {

    private final Map<String, String> args = new HashMap<>();

    @Test
    public void testAdIfValuePresent() {
        String key = UUID.randomUUID().toString();
        String val = UUID.randomUUID().toString();

        addIfValuePresent(args, key, val);

        assertThat(args.get(key), is(val));
    }

    @Test
    public void testAddIfValuePresentWithEmpty() {
        args.clear();

        addIfValuePresent(args, "", UUID.randomUUID().toString());

        assertThat(args.size(), is(0));
    }

    @Test
    public void testAddIfValuePresentNull() {
        args.clear();

        addIfValuePresent(args, null, UUID.randomUUID().toString());

        assertThat(args.size(), is(0));
    }

    @Test
    public void testAddHeaders() {
        args.clear();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String key = UUID.randomUUID().toString() + "ABC";
        String val = UUID.randomUUID().toString();
        String httpHeaderKey = LoggingConstants.HTTP_HEADER_PREFIX + key.toLowerCase(Locale.getDefault());
        headers.put(key, singletonList(val));

        addHeaders(args, new HttpHeaders(headers));

        assertThat(args.get(httpHeaderKey), is(val));
        assertThat(args.size(), is(1));
    }

}
