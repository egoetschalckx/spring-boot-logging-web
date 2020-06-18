package io.github.goetschalckx.spring.logging.web.server;

import io.github.goetschalckx.spring.logging.web.LoggingConstants;
import io.github.goetschalckx.spring.logging.web.span.SpanIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        prefix = LoggingConstants.LOGGING_PREFIX_SERVER,
        name = LoggingConstants.LOGGING_ENABLED,
        havingValue = "true")
public class ServerLoggingConfig {

    // TODO: make this @Value a ConfigurationProperty instead
    @Value(LoggingConstants.LOGGING_SERVER_RESPONSE_BODY_KEY)
    private boolean includeBody = false;

    @Bean
    @ConditionalOnMissingBean
    public RequestLoggingFilter requestLoggingFilter(
            ServerLogger serverLogger,
            SpanIdGenerator spanIdGenerator
    ) {
        return new RequestLoggingFilter(
                includeBody,
                serverLogger,
                spanIdGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerLogger serverLogger() {
        return new ServerLogger();
    }

}
