package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.provider;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.UnaryOperator;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.dto.response.TokenResponse;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Clock;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final Clock clock;

    public JwtTokenProvider (
        @Value("${jwt.secret.key}") String secretKey,
        Clock clock
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.clock = clock;
    }

    public TokenResponse provide(long expirationMinutes, UnaryOperator<JwtBuilder> template) {
        Instant now = clock.instant();
        Instant expiredAt = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        JwtBuilder builder = Jwts.builder()
            .setExpiration(Date.from(expiredAt))
            .setIssuedAt(Date.from(now))
            .signWith(secretKey);
        template.apply(builder);
        String accessToken = builder.compact();
        return new TokenResponse(accessToken, LocalDateTime.ofInstant(expiredAt, clock.getZone()));
    }
}
