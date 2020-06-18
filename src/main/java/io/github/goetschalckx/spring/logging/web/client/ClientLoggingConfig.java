package io.github.goetschalckx.spring.logging.web.client;

import io.github.goetschalckx.spring.logging.web.LoggingConstants;
import io.github.goetschalckx.spring.logging.web.span.SpanIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
@ConditionalOnProperty(
        prefix = LoggingConstants.LOGGING_PREFIX_CLIENT,
        name = LoggingConstants.LOGGING_ENABLED,
        havingValue = "true")
public class ClientLoggingConfig {

    // TODO: make this @Value a ConfigurationProperty instead
    @Value(LoggingConstants.LOGGING_CLIENT_RESPONSE_BODY_KEY)
    private boolean includeBody = false;

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        final SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        simpleClientHttpRequestFactory.setOutputStreaming(!includeBody);
        simpleClientHttpRequestFactory.setBufferRequestBody(!includeBody);

        return simpleClientHttpRequestFactory;
    }

    @Bean
    public LoggingInterceptorCustomizer loggingInterceptorCustomizer(
            ClientHttpRequestLoggingInterceptor clientHttpRequestLoggingInterceptor,
            ClientHttpRequestFactory clientHttpRequestFactory
    ) {
        return new LoggingInterceptorCustomizer(
                clientHttpRequestLoggingInterceptor,
                clientHttpRequestFactory,
                includeBody);
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientLogger clientLogger() {
        return new ClientLogger();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestLoggingInterceptor clientHttpRequestLoggingInterceptor(
            ClientLogger clientLogger,
            SpanIdGenerator spanIdGenerator
    ) {
        return new ClientHttpRequestLoggingInterceptor(
                includeBody,
                clientLogger,
                spanIdGenerator
        );
    }

}
