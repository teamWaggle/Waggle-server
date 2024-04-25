package com.example.waggle.web.dto.chat;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRequest {

    @NotEmpty
    private String name;
    private String description;
    private String password;

    @Builder
    public ChatRoomRequest(String name, String description, String password) {
        this.name = name;
        this.description = description;
        this.password = password;
    }

}
