package ru.practicum.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitStatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientStats {
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected final RestTemplate rest;

    public ClientStats(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<T> get(String path, @Nullable Map<String, Object> parameters, ParameterizedTypeReference<T> type) {
        return get(path, null, parameters, type);
    }

    protected <T> ResponseEntity<T> get(String path, @Nullable Long userId, @Nullable Map<String, Object> parameters, ParameterizedTypeReference<T> type) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null, type);
    }

    protected <T> ResponseEntity<T> post(String path, Object body, ParameterizedTypeReference<T> type) {
        return post(path, null, null, body, type);
    }

    protected <T> ResponseEntity<T> post(String path, long userId, Object body, ParameterizedTypeReference<T> type) {
        return post(path, userId, null, body, type);
    }

    protected <T> ResponseEntity<T> post(String path, Long userId, @Nullable Map<String, Object> parameters, Object body, ParameterizedTypeReference<T> type) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body, type);
    }

    private <T> ResponseEntity<T> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable Object body, ParameterizedTypeReference<T> type) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<T> shareitServerResponse;
        if (parameters != null) {
            shareitServerResponse = rest.exchange(path, method, requestEntity, type, parameters);
        } else {
            shareitServerResponse = rest.exchange(path, method, requestEntity, type);
        }

        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }

    private static <T> ResponseEntity<T> prepareGatewayResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    public ResponseEntity<List<EndpointHitStatDto>> getStatistics(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris", uris);
        params.put("unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", params, new ParameterizedTypeReference<>() {});
    }

    public void addHit(String app, String uri, String ip, LocalDateTime timestamp) {
        post("/hit", new EndpointHitDto(app, uri, ip, df.format(timestamp)), new ParameterizedTypeReference<Void>() {});
    }
}