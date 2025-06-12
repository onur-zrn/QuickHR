package com.quickhr.utility;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.quickhr.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.*;

@Component
public class JwtManager {
	RefreshTokenRepository refreshTokenRepository;
	@Value("${java17.jwt.secret-key}")
	private String secretKey;

	@Value("${java17.jwt.issuer}")
	private String issuer;

	private Long expTime =  15L * 60;;//15 dk


	public String generateToken(Long userId) {
		Algorithm algoritm = Algorithm.HMAC512(secretKey);
		String token = JWT.create()
				.withAudience()
				.withIssuer(issuer)
				.withIssuedAt(Instant.now())
				.withExpiresAt(Instant.now().plusSeconds(expTime))
				.withClaim("userId", userId)
				.withClaim("role", "admin")
				.withClaim("key", "value123")
				.sign(algoritm);
		return token;
	}

	public String generateAccessToken(Long userId) {
		return JWT.create()
				.withIssuer(issuer)
				.withIssuedAt(Instant.now())
				.withExpiresAt(Instant.now().plusSeconds(expTime))
				.withClaim("userId", userId)
				.sign(Algorithm.HMAC512(secretKey));
	}

	public Optional<Long> validateToken(String token) {
		try {
			Algorithm algoritm = Algorithm.HMAC512(secretKey);
			JWTVerifier verifier = JWT.require(algoritm).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			if (decodedJWT == null) {
				return Optional.empty();
			}
			Long userId = decodedJWT.getClaim("userId").asLong();
			return Optional.of(userId);
		} catch (IllegalArgumentException | JWTVerificationException e) {
			System.out.println(e.getMessage());
			return Optional.empty();
		}
	}
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}

}
