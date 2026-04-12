package ste.netbeans.jwteditor.service;

import ste.netbeans.jwteditor.service.JwtVerificationService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.*;

@DisplayName("JWT Verification Service")
public class JwtVerificationServiceTest {

    private final JwtVerificationService service = new JwtVerificationService();

    @Test
    @DisplayName("verify_valid_jwt_with_correct_secret")
    public void verify_valid_jwt_with_correct_secret() {
        String secret = "this_is_a_secret_that_is_longer_than_32_bytes_for_testing";
        String token = JWT.create()
                .withSubject("test")
                .sign(Algorithm.HMAC256(secret));

        JwtVerificationService.VerificationResult result = service.verify(token, secret);

        then(result.valid).isTrue();
        then(result.secretValid).isTrue();
    }

    @Test
    @DisplayName("reject_jwt_with_wrong_secret")
    public void reject_jwt_with_wrong_secret() {
        String secret1 = "this_is_a_secret_that_is_longer_than_32_bytes_first_secret";
        String secret2 = "this_is_a_secret_that_is_longer_than_32_bytes_second_secret";
        String token = JWT.create()
                .withSubject("test")
                .sign(Algorithm.HMAC256(secret1));

        JwtVerificationService.VerificationResult result = service.verify(token, secret2);

        then(result.valid).isFalse();
        then(result.secretValid).isTrue();
    }

    @Test
    @DisplayName("reject_secret_shorter_than_32_bytes")
    public void reject_secret_shorter_than_32_bytes() {
        String shortSecret = "short";

        then(service.isSecretValid(shortSecret)).isFalse();
    }

    @Test
    @DisplayName("accept_secret_with_32_or_more_bytes")
    public void accept_secret_with_32_or_more_bytes() {
        String secret32 = "a".repeat(32);
        String secret64 = "a".repeat(64);

        then(service.isSecretValid(secret32)).isTrue();
        then(service.isSecretValid(secret64)).isTrue();
    }

    @Test
    @DisplayName("return_error_for_empty_token")
    public void return_error_for_empty_token() {
        String secret = "a".repeat(32);

        JwtVerificationService.VerificationResult result = service.verify("", secret);

        then(result.valid).isFalse();
        then(result.secretValid).isTrue();
    }

    @Test
    @DisplayName("return_error_for_empty_secret")
    public void return_error_for_empty_secret() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";

        JwtVerificationService.VerificationResult result = service.verify(token, "");

        then(result.valid).isFalse();
        then(result.secretValid).isFalse();
    }
}
