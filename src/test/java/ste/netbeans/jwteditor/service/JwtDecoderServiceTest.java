package ste.netbeans.jwteditor.service;

import ste.netbeans.jwteditor.service.JwtDecoderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.*;

@DisplayName("JWT Decoder Service")
public class JwtDecoderServiceTest {

    private final JwtDecoderService service = new JwtDecoderService();

    @Test
    @DisplayName("should_decode_valid_jwt_token")
    public void should_decode_valid_jwt_token() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        JwtDecoderService.DecodeResult result = service.decode(token);

        then(result.valid).isTrue();
        then(result.parts).isNotNull();
        then(result.parts.headerMap).containsKeys("alg", "typ");
        then(result.parts.payloadMap).containsKeys("sub", "name", "iat");
    }

    @Test
    @DisplayName("should_return_error_for_empty_token")
    public void should_return_error_for_empty_token() {
        JwtDecoderService.DecodeResult result = service.decode("");

        then(result.valid).isFalse();
        then(result.error).isNotEmpty();
    }

    @Test
    @DisplayName("should_return_error_for_malformed_token")
    public void should_return_error_for_malformed_token() {
        JwtDecoderService.DecodeResult result = service.decode("invalid.token");

        then(result.valid).isFalse();
        then(result.error).contains("expected 3 parts");
    }

    @Test
    @DisplayName("should_return_error_for_invalid_base64")
    public void should_return_error_for_invalid_base64() {
        JwtDecoderService.DecodeResult result = service.decode("!!!.!!!.!!!");

        then(result.valid).isFalse();
        then(result.error).isNotEmpty();
    }

    @Test
    @DisplayName("should_format_exp_field_with_timestamp_and_datetime")
    public void should_format_exp_field_with_timestamp_and_datetime() {
        long timestamp = 1234567890L;
        String formatted = service.formatPayloadValue("exp", timestamp);

        then(formatted).contains("1234567890");
        then(formatted).contains("(");
        then(formatted).contains(")");
    }

    @Test
    @DisplayName("should_extract_header_correctly")
    public void should_extract_header_correctly() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";

        JwtDecoderService.DecodeResult result = service.decode(token);

        then(result.valid).isTrue();
        then(result.parts.headerMap.get("alg")).isEqualTo("HS256");
        then(result.parts.headerMap.get("typ")).isEqualTo("JWT");
    }

    @Test
    @DisplayName("should_null_token_be_handled_gracefully")
    public void should_null_token_be_handled_gracefully() {
        JwtDecoderService.DecodeResult result = service.decode(null);

        then(result.valid).isFalse();
        then(result.error).isNotEmpty();
    }
}
