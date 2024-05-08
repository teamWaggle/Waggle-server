package com.example.waggle.global.controller;

import com.example.waggle.exception.payload.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public ApiResponseDto<String> getProfile() {
        List<String> profile = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profile.isEmpty() ? "default" : profile.get(0);

        String result = profile.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);

        return ApiResponseDto.onSuccess(result);
    }
}
