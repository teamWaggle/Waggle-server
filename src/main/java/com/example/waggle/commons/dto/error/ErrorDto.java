package com.example.waggle.commons.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private int status;
    private String error;
    private String code;
    private String message;
}
