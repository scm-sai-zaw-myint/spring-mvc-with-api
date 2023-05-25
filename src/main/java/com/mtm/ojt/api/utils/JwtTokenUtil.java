package com.mtm.ojt.api.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mtm.ojt.api.commons.exception.TokenException;
import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.bl.services.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil implements Serializable {

    @Autowired
    UserService userService;

    /**
     * 
     */
    private static final long serialVersionUID = -362113638431139777L;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validity}")
    private long validity;

    public String createUserToken(UserDTO user, String tokenIP) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream().map(r -> r.getName()).toList());
        return generateToken(claims, String.format("%s,%s", user.getId(), user.getName()), tokenIP);
    }

    public UserDetails parseToken(String token) {
        String subject = getClaim(token, Claims::getSubject);
        UserDTO userDto = this.userService.doGetUserById(Integer.parseInt(subject.split(",")[0]));
        return userDto;
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public void validateAccessToken(String token) throws TokenException {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            UserDetails userDetail = this.parseToken(token);
            UserDTO user = this.userService.doGetUserByName(userDetail.getUsername());
            Claims claims = this.getAllClaims(token);
            if(user != null && !claims.getId().equals(user.getTokenIp())) {
                throw new TokenException("User logout or changed password!");
            }
        } catch (ExpiredJwtException | IllegalArgumentException | MalformedJwtException | UnsupportedJwtException
                | SignatureException ex) {
            throw new TokenException(ex.getMessage());
        }
    }

    private String generateToken(Map<String, Object> claims, String subject, String tokenIP) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuer("MTM_API_OJT")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setId(tokenIP)
                .setExpiration(new Date(System.currentTimeMillis() + (validity * 60) * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    
    private Claims getAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            return null;
        }
    }
}
