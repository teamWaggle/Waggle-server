package com.example.waggle.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    @Length(min=10)
    private String password;
}
