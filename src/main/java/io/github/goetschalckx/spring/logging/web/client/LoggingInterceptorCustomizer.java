package io.github.goetschalckx.spring.logging.web.client;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class LoggingInterceptorCustomizer implements RestTemplateCustomizer {

    private final ClientHttpRequestLoggingInterceptor clientHttpRequestLoggingInterceptor;
    private final ClientHttpRequestFactory requestFactory;
    private final boolean logResponseBody;

    public LoggingInterceptorCustomizer(
            ClientHttpRequestLoggingInterceptor clientHttpRequestLoggingInterceptor,
            ClientHttpRequestFactory requestFactory,
            boolean logResponseBody
    ) {
        this.clientHttpRequestLoggingInterceptor = clientHttpRequestLoggingInterceptor;
        this.requestFactory = requestFactory;
        this.logResponseBody = logResponseBody;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate
                .getInterceptors()
                .add(clientHttpRequestLoggingInterceptor);

        if (logResponseBody) {
            final ClientHttpRequestFactory bufferingClientHttpRequestFactory =
                    new BufferingClientHttpRequestFactory(requestFactory);

            restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        }
    }

}
