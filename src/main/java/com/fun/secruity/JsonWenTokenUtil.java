package com.fun.secruity;

import com.fun.model.AuthTokenDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonWenTokenUtil implements Serializable {

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_ROLE_NAME = "rolename";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public AuthTokenDetails getDetailsFromToken(String token) {
        AuthTokenDetails authTokenDetails = null;
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            authTokenDetails = new AuthTokenDetails();
            authTokenDetails.setUsername((String) claims.get(CLAIM_KEY_USERNAME));
            authTokenDetails.setRoleName((String) claims.get(CLAIM_KEY_ROLE_NAME));
            authTokenDetails.setExpirationDate(claims.getExpiration());
        }
        return authTokenDetails;
    }

    /**
     * 从token获取信息
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 过期时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * token 是否过期
     * @param token token
     * @return [true | false]
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        boolean isTokenExpired = expiration.before(new Date());
        if (isTokenExpired) {
            throw new AccountExpiredException("Token is out of date");
        }
        return isTokenExpired;
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(JwtUser jwtUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, jwtUser.getUsername());
        claims.put(CLAIM_KEY_ROLE_NAME, jwtUser.getAuthorities().toArray());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return createJsonWebToken(claims);
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = createJsonWebToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 创建新的 JsonWebToken
     * @param claims 数据声明
     * @return token
     */
    public String createJsonWebToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

    /**
     * 验证 token
     * @param token
     * @param userDetails
     * @return [true | false]
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }
}

