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
        String secret = "short_secret";
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
        String secret1 = "secret1";
        String secret2 = "secret2";
        String token = JWT.create()
                .withSubject("test")
                .sign(Algorithm.HMAC256(secret1));

        JwtVerificationService.VerificationResult result = service.verify(token, secret2);

        then(result.valid).isFalse();
        then(result.secretValid).isTrue();
    }

    @Test
    @DisplayName("accept_any_non_empty_secret")
    public void accept_any_non_empty_secret() {
        then(service.isSecretValid("a")).isTrue();
        then(service.isSecretValid("longer_secret")).isTrue();
    }

    @Test
    @DisplayName("reject_empty_secret")
    public void reject_empty_secret() {
        then(service.isSecretValid("")).isFalse();
        then(service.isSecretValid(null)).isFalse();
    }

    @Test
    @DisplayName("return_error_for_empty_token")
    public void return_error_for_empty_token() {
        String secret = "secret";

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
