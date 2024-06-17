package com.example.waggle.domain.chat.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {

    @NotEmpty(message = "채팅방 이름을 입력해 주세요.")
    @Size(max = 15, message = "채팅방 이름은 최대 15자까지 입력할 수 있습니다.")
    private String name;

    @Size(max = 40, message = "설명은 최대 40자까지 입력할 수 있습니다.")
    private String description;

    @Pattern(regexp = "\\d{6}", message = "비밀번호는 6자리 숫자여야 합니다.")
    private String password;

}
