package com.example.waggle.global.controller;

import com.example.waggle.exception.payload.dto.ApiResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public ApiResponseDto<Boolean> healthCheck() {
        return ApiResponseDto.onSuccess(true);
    }

}