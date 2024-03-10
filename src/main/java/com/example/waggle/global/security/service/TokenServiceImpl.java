package com.example.waggle.global.security.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.filter.JwtAuthenticationException;
import com.example.waggle.global.exception.handler.AuthenticationHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.object.JwtToken;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider.WAGGLE;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {


    private final Key key;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final MemberQueryService memberQueryService;

    public TokenServiceImpl(@Value("${app.jwt.secret}") String key,
                            AuthenticationManagerBuilder authenticationManagerBuilder,
                            PasswordEncoder passwordEncoder,
                            RedisService redisService,
                            MemberQueryService memberQueryService) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.memberQueryService = memberQueryService;
    }

    @Override
    public JwtToken login(MemberCredentialsDto loginRequest) {
        Member member = memberQueryService.getMemberByEmail(loginRequest.getEmail());
        if (!member.getAuthProvider().equals(WAGGLE)) {
            throw new AuthenticationHandler(ErrorStatus.AUTH_PROVIDER_IS_NOT_MATCH);
        }
        Authentication authentication;
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {

            throw new AuthenticationHandler(ErrorStatus.AUTH_MISMATCH_EMAIL_AND_PASSWORD);
        }
        authentication = new UsernamePasswordAuthenticationToken(member, "",
                member.getAuthorities());

        JwtToken jwtToken = generateToken(authentication);
        jwtToken.setMember(MemberConverter.toMemberSummaryDto(member));
        return jwtToken;
    }

    @Override
    public JwtToken issueTokens(String refreshToken) {
        // Refresh Token 유효성 검사
        if (!validateToken(refreshToken) || !existsRefreshToken(refreshToken)) {
            throw new GeneralException(ErrorStatus.AUTH_INVALID_REFRESH_TOKEN);
        }

        // 이전 리프레시 토큰 삭제
        redisService.deleteValue(refreshToken);

        // 새로운 Authentication 객체 생성
        Claims claims = parseClaims(refreshToken);
        String username = claims.getSubject();
        Member member = memberQueryService.getMemberByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, "",
                member.getAuthorities());

        // 새 토큰 생성
        JwtToken newTokens = generateToken(authentication);
        newTokens.setMember(MemberConverter.toMemberSummaryDto(member));

        return newTokens;
    }

    @Override
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 1800000);   // 30분
        log.info("date = {}", accessTokenExpiresIn);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(now + 604800000))    // 7일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 새 리프레시 토큰을 Redis에 저장
        redisService.setValue(refreshToken, authentication.getName());

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new JwtAuthenticationException(ErrorStatus.AUTH_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new JwtAuthenticationException(ErrorStatus.AUTH_TOKEN_HAS_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new JwtAuthenticationException(ErrorStatus.AUTH_TOKEN_IS_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new JwtAuthenticationException(ErrorStatus.AUTH_IS_NULL);
        }
    }

    @Override
    public boolean logout(String refreshToken) {
        redisService.deleteValue(refreshToken);
        return true;
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // RefreshToken 존재유무 확인
    @Override
    public boolean existsRefreshToken(String refreshToken) {
        return redisService.getValue(refreshToken) != null;
    }
}