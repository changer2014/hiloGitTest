package com.hilo.service.api.confiuration.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hilo.service.api.model.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

  /**
   * The constant ACCESS_TOKEN_VALID_TIME.
   */
  private static final long ACCESS_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000;

  /**
   * The constant REFRESH_TOKEN_VALID_TIME.
   */
  private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000;

  /**
   * The constant SIGN_KEY.
   */
  private static final String SIGN_KEY = "ScooterJWTSecret";

  /**
   * The constant status of user have unfinished trip
   */
  public static final String USER_STATUS_UNFINISHED_TRIP = "1";

  /**
   * The constant status of user have unfinished order
   */
  public static final String USER_STATUS_UNFINISHED_ORDER = "2";


  /**
   * Gets token.
   *
   * @param user the user
   * @return the token
   */
  public String getAccessToken(UserInfo user) {
    Map<String, Object> claims = new HashMap<>(4);
    claims.put("id", user.getId());
    claims.put("phone", user.getPhone());
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getPhone())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS512, SIGN_KEY)
            .compact();
  }

  /**
   * Gets refresh token.
   *
   * @param phone the phone
   * @return the refresh token
   */
  public String getRefreshToken(String phone) {
    Map<String, Object> claims = new HashMap<>(4);
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(phone)
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS512, SIGN_KEY)
            .compact();
  }


  /**
   * Gets user.
   *
   * @param token the token
   * @return the user
   * @throws InvalidJwtTokenException the invalid jwt token exception
   */
  public UserInfo parseAccessToken(String token) throws InvalidJwtTokenException {
    try {
      Claims claims = Jwts.parser().setSigningKey(SIGN_KEY)
              .parseClaimsJws(token)
              .getBody();
      String phone = claims.getSubject();
      Date expiration = claims.getExpiration();
      if (System.currentTimeMillis() > expiration.getTime() || phone == null) {
        throw new InvalidJwtTokenException("Invalid access token");
      }
      UserInfo user = new UserInfo();
      user.setPhone(phone);
      user.setId(claims.get("id").toString());
      return user;
    } catch (Exception e) {
      throw new InvalidJwtTokenException("Invalid access token");
    }
  }

  /**
   * Parse refresh token string.
   *
   * @param token the token
   * @return the string
   * @throws InvalidJwtTokenException the invalid jwt token exception
   */
  public String parseRefreshToken(String token) throws InvalidJwtTokenException {
    try {
      Claims claims = Jwts.parser().setSigningKey(SIGN_KEY)
              .parseClaimsJws(token)
              .getBody();
      Date expiration = claims.getExpiration();
      if (System.currentTimeMillis() > expiration.getTime()) {
        throw new InvalidJwtTokenException("Invalid refresh token");
      }
      return claims.getSubject();
    } catch (Exception e) {
      throw new InvalidJwtTokenException("Invalid refresh token");
    }
  }

}
