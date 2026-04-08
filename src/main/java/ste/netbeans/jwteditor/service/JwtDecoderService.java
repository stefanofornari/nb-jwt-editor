package ste.netbeans.jwteditor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class JwtDecoderService {

    public static class JwtParts {
        public final String header;
        public final String payload;
        public final String signature;
        public final Map<String, Object> headerMap;
        public final Map<String, Object> payloadMap;

        public JwtParts(String header, String payload, String signature,
                       Map<String, Object> headerMap, Map<String, Object> payloadMap) {
            this.header = header;
            this.payload = payload;
            this.signature = signature;
            this.headerMap = headerMap;
            this.payloadMap = payloadMap;
        }
    }

    public static class DecodeResult {
        public final boolean valid;
        public final JwtParts parts;
        public final String error;

        public DecodeResult(JwtParts parts) {
            this.valid = true;
            this.parts = parts;
            this.error = null;
        }

        public DecodeResult(String error) {
            this.valid = false;
            this.parts = null;
            this.error = error;
        }
    }

    public DecodeResult decode(String token) {
        if (token == null || token.trim().isEmpty()) {
            return new DecodeResult("Token is empty");
        }

        token = token.trim();
        String[] parts = token.split("\\.");

        if (parts.length != 3) {
            return new DecodeResult("Invalid token format: expected 3 parts separated by dots");
        }

        try {
            String headerJson = decodeBase64Part(parts[0]);
            String payloadJson = decodeBase64Part(parts[1]);

            Map<String, Object> headerMap = jsonToMap(new JSONObject(headerJson));
            Map<String, Object> payloadMap = jsonToMap(new JSONObject(payloadJson));

            return new DecodeResult(new JwtParts(headerJson, payloadJson, parts[2], headerMap, payloadMap));
        } catch (Exception e) {
            return new DecodeResult("Failed to decode token: " + e.getMessage());
        }
    }

    public String formatPayloadValue(String key, Object value) {
        if ("exp".equals(key) && value instanceof Number) {
            long timestamp = ((Number) value).longValue();
            Instant instant = Instant.ofEpochSecond(timestamp);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return timestamp + " (" + dateTime + ")";
        }
        return String.valueOf(value);
    }

    private String decodeBase64Part(String part) {
        byte[] decoded = Base64.getUrlDecoder().decode(addPadding(part));
        return new String(decoded, StandardCharsets.UTF_8);
    }

    private String addPadding(String input) {
        int remainder = input.length() % 4;
        if (remainder > 0) {
            input += "=".repeat(4 - remainder);
        }
        return input;
    }

    private Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : json.keySet()) {
            map.put(key, json.get(key));
        }
        return map;
    }
}
