package com.creativedesignproject.kumoh_board_backend.auth.infrastructure;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.common.exception.ErrorCode;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenParser {
    private final JwtParser jwtParser;

    public JwtTokenParser(
        @Value("${secret.key}") String secretKey,
        Clock clock
    ) {
        this.jwtParser = Jwts.parserBuilder()
            .setClock(() -> Date.from(clock.instant()))
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build();
    }

    public Claims getClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_AUTH_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_AUTH_TOKEN);
        } catch (Exception e) {
            log.error("JWT 토큰 파싱 중에 문제가 발생했습니다.");
            throw e;
        }
    }
}
