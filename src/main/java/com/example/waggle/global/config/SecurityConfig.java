package com.example.waggle.global.config;


import com.example.waggle.global.security.JwtAuthenticationFilter;
import com.example.waggle.global.security.exception.JwtAccessDeniedHandler;
import com.example.waggle.global.security.exception.JwtAuthenticationEntryPoint;
import com.example.waggle.global.security.oauth2.CustomOAuth2UserService;
import com.example.waggle.global.security.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.example.waggle.global.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;
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


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //cors(다른 자원 허용)
                .cors().and()
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
                .authorizeHttpRequests()
                // MEMBER
                .requestMatchers(HttpMethod.PUT, "/api/members").authenticated()
                .requestMatchers("/api/members/**").permitAll()
//                // TEAM
                .requestMatchers(HttpMethod.GET, "/api/teams/**").permitAll()
//                // REPLY
                .requestMatchers(HttpMethod.GET, "/api/replies/**").permitAll()
//                // COMMENT
                .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
//                // SCHEDULE
                .requestMatchers(HttpMethod.GET, "/api/schedules/**").permitAll()
//                // PET
                .requestMatchers(HttpMethod.GET, "/api/pets/**").permitAll()
//                // STORY
                .requestMatchers(HttpMethod.GET, "/api/stories/**").permitAll()
//                // QUESTION
                .requestMatchers(HttpMethod.GET, "/api/questions/**").permitAll()
//                // HELP
                .requestMatchers(HttpMethod.GET, "/api/sirens/**").permitAll()
//                // RECOMMEND
                .requestMatchers(HttpMethod.GET, "/api/recommends/**").permitAll()
//                // ANSWER
                .requestMatchers(HttpMethod.GET, "/api/answers/**").permitAll()
//                // FOLLOW
                .requestMatchers(HttpMethod.GET, "/api/follows/**").permitAll()
                // OAUTH2
                .requestMatchers("/oauth2/**", "/login/**").permitAll()
                // TOKEN
                .requestMatchers(HttpMethod.POST, "/api/tokens").permitAll()
                .requestMatchers("/api/tokens/oauth2").permitAll()
                // SWAGGER
                .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                //profile
                .requestMatchers("/profile").permitAll()
                // ELSE
                //정적 페이지 허가
                .requestMatchers("/", "/.well-known/**", "/css/**", "/*.ico", "/error", "/images/**").permitAll() // 임시로 모든 API 허용
                .anyRequest().authenticated();

        //OAUTH2
        httpSecurity
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorize")  // 소셜 로그인 url
                .and()
                //userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정한다.
                .userInfoEndpoint().userService(customOAuth2UserService)  // 회원 정보 처리
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        //order : jwtFilter -> usernamePasswordAuthentication
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //authentication/authorization
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)    // 401
                .accessDeniedHandler(jwtAccessDeniedHandler);        // 403


        return httpSecurity.build();
    }
}

