package com.supercoding.hanyipman.security;

import com.supercoding.hanyipman.entity.User;
import com.supercoding.hanyipman.error.CustomException;
import com.supercoding.hanyipman.error.domain.TokenErrorCode;
import com.supercoding.hanyipman.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static String secretKeySource;
    @Value("${secret-key-source}")
    public void setSecretKeySource(String value) {
        secretKeySource = value;
    }
    private final CustomUserDetailService customUserDetailService;

    public static String createToken(User loginUser) {

        Claims claims = Jwts.claims().setSubject(loginUser.getEmail());
        Date now = new Date();
        // 토큰 만료시간 48시간
        Date tokenValidMillisecond = Date.from(Instant.now().plus(48, ChronoUnit.HOURS));
        claims.put("userIdx", loginUser.getId());
        claims.put("email", loginUser.getEmail());
        claims.put("role", loginUser.getRole());
        claims.put("nickname", loginUser.getNickname());

        return Jwts.builder()
                .setSubject(loginUser.getEmail())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(tokenValidMillisecond)
                .signWith(SignatureAlgorithm.HS256, secretKeySource)
                .compact();
    }

    // 토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 객체반환
    public Authentication getAuthentication(String jwtToken) {
        String userEmail = Jwts.parser().setSigningKey(secretKeySource).parseClaimsJws(jwtToken).getBody().get("email").toString();
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, jwtToken, userDetails.getAuthorities());
    }

    // 토큰을 검증함
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKeySource).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new CustomException(TokenErrorCode.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new CustomException(TokenErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(TokenErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(TokenErrorCode.INVALID_JWT_TOKEN);
        } catch (SignatureException e) {
            throw new CustomException(TokenErrorCode.INVALID_JWT_SIGNATURE);
        }
    }


    public String getUserEmail(String jwtToken) {
        return Jwts.parser().setSigningKey(secretKeySource).parseClaimsJws(jwtToken).getBody().get("email").toString();
    }


}

