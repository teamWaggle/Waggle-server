package com.example.waggle.web.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInDto {
    @NotBlank(message = "아이디를 작성해주세요.")
    private String username;
    @NotBlank(message = "비밀번호를 작성해주세요.")
    private String password;
}
