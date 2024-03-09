package com.example.waggle.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST"), USER("ROLE_USER"), DORMANT("ROLE_DORMANT"), ADMIN("ROLE_ADMIN");

    private final String key;
}
