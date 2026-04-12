package ste.netbeans.jwteditor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;


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

    public boolean isSecretValid(String secret) {
        return secret != null && !secret.isEmpty();
    }

    public VerificationResult verify(String token, String secret) {
        if (token == null || token.trim().isEmpty()) {
            return new VerificationResult(false, "Token is empty", isSecretValid(secret));
        }

        if (secret == null || secret.isEmpty()) {
            return new VerificationResult(false, "Secret is required", false);
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
