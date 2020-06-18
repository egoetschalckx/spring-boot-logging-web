package io.github.goetschalckx.spring.logging.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static net.logstash.logback.argument.StructuredArguments.v;

//@SpringBootTest
@SpringBootApplication
public class TestApplication {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestApplication.class);

    @Autowired
    private RestTemplate restTemplate;

    //@Autowired
    //private org.springframework.core.env.Environment springEnv;

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class)
                .bannerMode(Banner.Mode.OFF)
                //.profiles("log-ndjson")
                .build()
                .run(args);
    }

    //@Test
    public void getHeartbeats() {
        String url = "http://localhost:8080/heartbeat?foo=bar";

        int x = 1;

        for (long i = 0; i < x; i++) {
            ResponseEntity<HeartbeatResponse> responseEntity = getHeartbeat(url);
            HeartbeatResponse response = responseEntity.getBody();
            Instant time = response.time;
            log.info("Got heartbeat {} {}",
                    v("heartbeat", time),
                    v("rnd", ThreadLocalRandom.current().nextInt(1, 4)));
        }
    }

    public ResponseEntity<HeartbeatResponse> getHeartbeat(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + UUID.randomUUID().toString());
        headers.set("Date", DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC)));

        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Eric");
        body.put("lastName", "G");
        body.put("rnd", Integer.toString(ThreadLocalRandom.current().nextInt(1, 5)));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body ,headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                HeartbeatResponse.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
