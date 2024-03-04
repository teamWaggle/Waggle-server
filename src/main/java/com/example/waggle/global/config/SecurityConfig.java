package com.example.waggle.global.config;


import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.example.waggle.global.security.exception.JwtAccessDeniedHandler;
import com.example.waggle.global.security.exception.JwtAuthenticationEntryPoint;
import com.example.waggle.global.security.filter.JwtAuthenticationFilter;
import com.example.waggle.global.security.filter.JwtExceptionFilter;
import com.example.waggle.global.security.oauth2.CustomOAuth2UserService;
import com.example.waggle.global.security.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.example.waggle.global.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        configureCorsAndSecurity(httpSecurity);
        configureAuthorization(httpSecurity);
        configureOAuth2(httpSecurity);
        addFilter(httpSecurity);
        configureExceptionHandling(httpSecurity);

        return httpSecurity.build();
    }

    private void addFilter(HttpSecurity httpSecurity) {
        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

    }

    private void configureExceptionHandling(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)    // 401
                .accessDeniedHandler(jwtAccessDeniedHandler);        // 403
    }

    private void configureOAuth2(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorize")  // 소셜 로그인 url
                .and()
                //userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정한다.
                .userInfoEndpoint().userService(customOAuth2UserService)  // 회원 정보 처리
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
    }

    private void configureAuthorization(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers(additionalSwaggerRequests()).permitAll()
                .requestMatchers(authRelatedEndpoints()).permitAll()
                .requestMatchers(permitAllRequest()).permitAll()
//                .requestMatchers(authenticatedEndpoints()).authenticated()
                //정적 페이지 허가
                .requestMatchers("/", "/.well-known/**", "/css/**", "/*.ico", "/error", "/images/**")
                .permitAll() // 임시로 모든 API 허용
                .anyRequest().authenticated();
    }

    private static void configureCorsAndSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //cors(다른 자원 허용)
                .cors().and()
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private RequestMatcher[] additionalSwaggerRequests() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher("/swagger-ui/**"),
                antMatcher("/swagger-ui"),
                antMatcher("/swagger-ui.html"),
                antMatcher("/swagger/**"),
                antMatcher("/swagger-resources/**"),
                antMatcher("/v3/api-docs/**"),
                antMatcher("/profile")

        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    private RequestMatcher[] authRelatedEndpoints() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher("/oauth2/**"),
                antMatcher("/login/**"),
                antMatcher(HttpMethod.POST, "/api/tokens"),
                antMatcher(HttpMethod.POST, "/api/tokens/refresh"),
                antMatcher(HttpMethod.GET, "/api/tokens/oauth2"),
                antMatcher(HttpMethod.DELETE, "/api/tokens"),
                antMatcher(HttpMethod.GET, "/api/members/**"),
                antMatcher(HttpMethod.POST, "/api/members/**"),
                antMatcher("/api/members/{memberId}/password")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

//    private RequestMatcher[] authenticatedEndpoints() {
//        List<RequestMatcher> requestMatchers = List.of(
//                antMatcher(HttpMethod.PUT, "/api/members"),
//                antMatcher("/api/members/info")
//        );
//        return requestMatchers.toArray(RequestMatcher[]::new);
//    }

    private RequestMatcher[] permitAllRequest() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(HttpMethod.GET, "/api/teams/**"),
                antMatcher(HttpMethod.GET, "/api/replies/**"),
                antMatcher(HttpMethod.GET, "/api/comments/**"),
                antMatcher(HttpMethod.GET, "/api/schedules/**"),
                antMatcher(HttpMethod.GET, "/api/pets/**"),
                antMatcher(HttpMethod.GET, "/api/stories/**"),
                antMatcher(HttpMethod.GET, "/api/questions/**"),
                antMatcher(HttpMethod.GET, "/api/sirens/**"),
                antMatcher(HttpMethod.GET, "/api/recommends/**"),
                antMatcher(HttpMethod.GET, "/api/answers/**"),
                antMatcher(HttpMethod.GET, "/api/follows/**")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }
}

