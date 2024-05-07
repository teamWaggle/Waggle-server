package com.example.waggle.security.oauth.factory;

import com.example.waggle.security.oauth.dto.info.GoogleOAuth2User;
import com.example.waggle.security.oauth.dto.info.KakaoOAuth2User;
import com.example.waggle.security.oauth.dto.info.NaverOAuth2User;
import com.example.waggle.security.oauth.dto.info.OAuth2UserInfo;
import lombok.Getter;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        switch (authProvider) {
            case GOOGLE:
                return new GoogleOAuth2User(attributes);
            case NAVER:
                return new NaverOAuth2User(attributes);
            case KAKAO:
                return new KakaoOAuth2User(attributes);

            default:
                throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }

    @Getter
    public enum AuthProvider {
        GOOGLE, KAKAO, NAVER, WAGGLE
    }
}
