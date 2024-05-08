package com.example.waggle.security.oauth.dto.info;

import java.util.Map;

public class NaverOAuth2User extends OAuth2UserInfo {
    public NaverOAuth2User(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getOAuth2Id() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

}
