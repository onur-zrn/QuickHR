package com.quickhr.utility;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.quickhr.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.*;

@Component
public class JwtManager {
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	@Value("${java17.jwt.secret-key}")
	private String secretKey;

	@Value("${java17.jwt.issuer}")
	private String issuer;
	//private final long accessTokenExpiration =  15L * 60 * 10;//15 dk * 10 deneme sonra sil
	//private final long refreshTokenExpiration = 7 * 24 * 60 * 60; // 7 gün
	private final long accessTokenExpiration =  60L * 1000 * 15; // 1 saat.
	private final long refreshTokenExpiration = 60L * 60 * 60 * 60;


	public String generateToken(Long authId) {
		Algorithm algoritm = Algorithm.HMAC512(secretKey);
		String token = JWT.create()
				.withAudience()
				.withIssuer(issuer)
				.withIssuedAt(Instant.now())
				.withExpiresAt(Instant.now().plusSeconds(accessTokenExpiration))
				.withClaim("authId", authId)
				.withClaim("role", "role")
				.withClaim("key", "key")
				.sign(algoritm);
		return token;
	}

	public String generateAccessToken(Long authId, String role) {
		return JWT.create()
				.withIssuer(issuer)
				.withIssuedAt(Instant.now())
				.withExpiresAt(Instant.now().plusSeconds(accessTokenExpiration))
				.withClaim("authId", authId)
				.withClaim("role", role)
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
			Long authId = decodedJWT.getClaim("authId").asLong();
			return Optional.of(authId);
		} catch (IllegalArgumentException | JWTVerificationException e) {
			System.out.println(e.getMessage());
			return Optional.empty();
		}
	}

	@Transactional
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}

	public Optional<String> getRoleFromToken(String token) {
		try {
			Algorithm algoritm = Algorithm.HMAC512(secretKey);
			JWTVerifier verifier = JWT.require(algoritm).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			if (decodedJWT == null) {
				return Optional.empty();
			}
			String role = decodedJWT.getClaim("role").asString();
			return Optional.ofNullable(role);
		} catch (IllegalArgumentException | JWTVerificationException e) {
			System.out.println(e.getMessage());
			return Optional.empty();
		}
	}

}