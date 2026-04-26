package com.jenkins.saas.security;

import com.jenkins.saas.exceptions.UnauthorizedException;
import com.jenkins.saas.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtProperties jwtProperties;
    private  PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(this.jwtProperties.getPrivateKeyPath());
            this.publicKey = loadPublicKey(this.jwtProperties.getPublicKeyPath());
            log.info("private && public key loaded successfully");

        } catch (final Exception e) {
            log.error("Failed to load private key", e);
            throw new RuntimeException("Error loading private key", e);

        }
    }


    public String generateAccessToken(
            @NonNull
            final String tenantId,
            final String userId,
            final String role
    ) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + this.jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .subject(userId)
                .claim("tenant_id", tenantId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .issuer("stock-saas-app")
                .signWith(this.privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String getUserIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }


    public String getTenantIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("tenant_id", String.class);
    }


    public String getRoleFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    public boolean validateToken(final String token) {

        try {
            Jwts.parser()
                    .setSigningKey(this.publicKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token has expired");
     /*   } catch (final UnsupportedJwtException e) {
            throw new UnauthorizedException("Token is not supported");*/
        } catch (final MalformedJwtException e) {
            throw new UnauthorizedException("Malformed token");
        } catch (final SecurityException e) {
            throw new UnauthorizedException("Invalid JWT Signature");
        } catch (final IllegalArgumentException e) {
            throw new UnauthorizedException("JWT claims string is empty");
        }
    }



    private Claims getClaimsFromToken(final String token) {
        return Jwts.parser()
                .setSigningKey(this.publicKey)
                .build()
                .parseClaimsJws(token);
//  return Jwts.parser()
//                .verifyWith(this.publicKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();

    }

    private PrivateKey loadPrivateKey(final String privateKeyPath) throws Exception {
        try (final InputStream in = JwtService.class.getClassLoader().getResourceAsStream(privateKeyPath)) {
            if (in == null) {
                throw new RuntimeException("Can't load private key from" + privateKeyPath);
            }
            final String key = new String(in.readAllBytes());
            final String privateKeyPem = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] encoded = Base64.getDecoder().decode(privateKeyPem);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }
    }

    private PublicKey loadPublicKey(final String publicKeyPath) throws Exception {

        try (final InputStream in = JwtService.class.getClassLoader().getResourceAsStream(publicKeyPath)) {
            if (in == null) {
                throw new RuntimeException("Can't load public key from " + publicKeyPath);
            }
            final String key = new String(in.readAllBytes());
            final String publicKeyPem = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] encoded = Base64.getDecoder().decode(publicKeyPem);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        }

    }
}
