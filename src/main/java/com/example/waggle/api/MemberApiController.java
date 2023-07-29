package com.example.waggle.api;

import com.example.waggle.component.file.FileStore;
import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;
    private final FileStore fileStore;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        JwtToken jwtToken = memberService.signIn(signInDto);

        // 로그인 성공 시
        if (jwtToken != null) {
            Cookie cookie = new Cookie("access_token", jwtToken.getAccessToken());
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            return new ResponseEntity<>(new ApiResponse<>(jwtToken.getAccessToken()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpDto signUpDto) throws IOException {
        // 회원가입
        memberService.signUp(signUpDto);

        // 프로필 사진 설정
        UploadFile uploadFile = fileStore.storeFile(signUpDto.getProfileImg());
        if (uploadFile != null) {
            memberService.changeProfileImg(signUpDto.getUsername(), uploadFile);
        } else {
            // TODO 기본 프로필 사진

        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK), HttpStatus.OK);
    }
}
