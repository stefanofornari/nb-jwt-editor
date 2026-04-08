package ste.netbeans.jwteditor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.nio.charset.StandardCharsets;

public class JwtVerificationService {

    public static class VerificationResult {
        public final boolean valid;
        public final String message;
        public final boolean secretValid;

        public VerificationResult(boolean valid, String message, boolean secretValid) {
            this.valid = valid;
            this.message = message;
            this.secretValid = secretValid;
        }
    }

    private static final int MIN_SECRET_BYTES = 32;

    public boolean isSecretValid(String secret) {
        if (secret == null || secret.isEmpty()) {
            return false;
        }
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        return secretBytes.length >= MIN_SECRET_BYTES;
    }

    public VerificationResult verify(String token, String secret) {
        if (token == null || token.trim().isEmpty()) {
            return new VerificationResult(false, "Token is empty", isSecretValid(secret));
        }

        if (secret == null || secret.isEmpty()) {
            return new VerificationResult(false, "Secret is required", false);
        }

        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < MIN_SECRET_BYTES) {
            return new VerificationResult(false, "Secret must be at least " + MIN_SECRET_BYTES + " bytes", false);
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm).build().verify(token.trim());
            return new VerificationResult(true, "Signature is valid", true);
        } catch (JWTVerificationException e) {
            return new VerificationResult(false, "Signature verification failed: " + e.getMessage(), true);
        } catch (Exception e) {
            return new VerificationResult(false, "Verification error: " + e.getMessage(), true);
        }
    }
}
