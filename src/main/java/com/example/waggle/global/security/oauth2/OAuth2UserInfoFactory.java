package com.example.waggle.global.security.oauth2;

import com.example.waggle.global.security.oauth2.info.GoogleOAuth2User;
import com.example.waggle.global.security.oauth2.info.KakaoOAuth2User;
import com.example.waggle.global.security.oauth2.info.NaverOAuth2User;
import com.example.waggle.global.security.oauth2.info.OAuth2UserInfo;
import lombok.Getter;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        switch (authProvider) {
            case GOOGLE: return new GoogleOAuth2User(attributes);
            case NAVER: return new NaverOAuth2User(attributes);
            case KAKAO: return new KakaoOAuth2User(attributes);

            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
    @Getter
    public enum AuthProvider {
        GOOGLE, KAKAO, NAVER, WAGGLE
    }
}
