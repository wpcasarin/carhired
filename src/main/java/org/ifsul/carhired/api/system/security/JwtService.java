package org.ifsul.carhired.api.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class JwtService {
  private static final String SECRET_KEY = "2894b642f046454576a662e41376f385d7b71ca3775";
//  private static final Key SECRET_KEY;
//
//  static {
//    try {
//      SECRET_KEY = generateKey();
//    } catch (NoSuchAlgorithmException e) {
//      throw new RuntimeException(e);
//    }
//  }

//  private static Key generateKey() throws NoSuchAlgorithmException {
//    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//    keyPairGenerator.initialize(2048);
//    KeyPair keys = keyPairGenerator.generateKeyPair();
//    return keys.getPrivate();
//  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }


  public String generateToken(
      Map<String, Object> extraClaims,
      @NonNull UserDetails userDetails
  ) {
    Instant now = Instant.now();
    Instant expires = now.plus(24, HOURS);

    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expires))
        .signWith(getSignInKey(), HS256)
        .compact();
  }

  public boolean isTokenValid(String token, @NonNull UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    Date expires = extractClaim(token, Claims::getExpiration);
    return expires.before(new Date());
  }


  private <T> T extractClaim(String token, @NonNull Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  @SneakyThrows
  private @NonNull Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
//    return SECRET_KEY;
  }
}
