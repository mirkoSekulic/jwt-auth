package com.auth.jwtauth.modules.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenUtil {
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_AUTHORITIES = "authorities";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public TokenUtil() {
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }

        return false;
    }

    public Authentication getAuthentication(String authToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(authToken)
                .getBody();

        String username = claims.getSubject();

        log.debug("Logging in: {}", username);

        Collection<? extends GrantedAuthority> authorities;

        if (claims.get(CLAIM_KEY_AUTHORITIES) != null && !claims.get(CLAIM_KEY_AUTHORITIES).toString().isEmpty()) {
            authorities = Arrays.stream(claims.get(CLAIM_KEY_AUTHORITIES).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            authorities = Collections.emptyList();
        }

        org.springframework.security.core.userdetails.User principal =
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
    }

    private LocalDateTime getCreatedDateFromClaims(Claims claims) {
        return Instant.ofEpochMilli(((Long) claims.get(CLAIM_KEY_CREATED)))
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public String createToken(Authentication authentication) {
        String authorities = prepareAuthoritiesFromAuthentication(authentication);

        Date validity = generateExpirationDate();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(CLAIM_KEY_AUTHORITIES, authorities)
                .claim(CLAIM_KEY_CREATED, getCreatedDate())
                .signWith(prepareSecretKey())
                .setExpiration(validity)
                .compact();
    }

    private Date getCreatedDate() {
        return Date.from(Instant.now());
    }

    private String prepareAuthoritiesFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Key prepareSecretKey() {
        Keys.secretKeyFor(SignatureAlgorithm.HS512);
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
