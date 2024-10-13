package org.example.interview.user.infrastructure.configuration;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.interview.user.domain.user.model.User;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;

import java.util.*;

@Slf4j
public class JwtService {

    private static final int EXPIRED_TIME = 600000000;
    private static final String SECRET_KEY = "secret_key";

    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getGeneralInfo().getEmail());
        claims.put("id", user.getId());
        claims.put("uuid", UUID.randomUUID());

        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRED_TIME))
                .setClaims(claims)
                .setSubject(user.getGeneralInfo().getEmail())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(header) && header.startsWith("Bearer ")) {
            var token = header.substring(7);
            if (StringUtils.isNotBlank(token)) return token;
        }
        return null;
    }

    public static boolean validate(String token, HttpServletRequest request) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT token");
            request.setAttribute("tokenError", "Expired JWT token");
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT token");
            request.setAttribute("tokenError", "Unsupported JWT token");
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT token");
            request.setAttribute("tokenError", "Invalid JWT token");
        } catch (SignatureException exception) {
            log.error("Signature JWT token");
            request.setAttribute("tokenError", "Signature JWT token");
        }
        return false;
    }

    public static Pair<String, UUID> extractClaims(String token) {
        var claims = extractAllClaims(token);
        String email = claims.get("email", String.class);
        UUID id = Optional.ofNullable(claims.get("id", String.class)).map(UUID::fromString).orElse(null);
        return Pair.of(email, id);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
