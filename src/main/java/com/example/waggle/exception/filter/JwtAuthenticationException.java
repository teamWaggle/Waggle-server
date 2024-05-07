package com.example.waggle.exception.filter;


import com.example.waggle.exception.payload.code.ErrorStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(ErrorStatus errorStatus) {
        super(errorStatus.name());
    }
}
