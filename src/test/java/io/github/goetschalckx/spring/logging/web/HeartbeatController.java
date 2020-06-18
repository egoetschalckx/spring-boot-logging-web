package io.github.goetschalckx.spring.logging.web;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@ResponseBody
@RequestMapping("heartbeat")
public class HeartbeatController {

    private final RestTemplate restTemplate;

    public HeartbeatController(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody HeartbeatResponse heartbeat() {
        //ResponseEntity<HeartbeatResponse> responseResponseEntity = getHeartbeat();

        return new HeartbeatResponse(Instant.now());
    }

    private ResponseEntity<HeartbeatResponse> getHeartbeat() {
        String url = "https://postman-echo.com/post";
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

}
