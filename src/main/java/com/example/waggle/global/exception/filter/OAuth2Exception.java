package com.example.waggle.global.exception.filter;

import com.example.waggle.global.payload.code.ErrorStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2Exception extends OAuth2AuthenticationException {

    public OAuth2Exception(ErrorStatus errorStatus) {
        super(errorStatus.name());
    }
}
