package com.example.waggle.api;

import com.example.waggle.component.file.FileStore;
import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member API", description = "회원 API")
public class MemberApiController {

    private final MemberService memberService;
    private final FileStore fileStore;

    @Operation(
            summary = "로그인",
            description = "아이디와 비밀번호를 통해 로그인을 진행합니다. 성공 시 엑세스 토큰을 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공. 엑세스 토큰을 반환합니다."
    )
    @ApiResponse(
            responseCode = "401",
            description = "로그인 실패. 인증되지 않음."
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        JwtToken jwtToken = memberService.signIn(signInDto);

        if (jwtToken != null) {
            Cookie cookie = new Cookie("access_token", jwtToken.getAccessToken());
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            return ResponseEntity.ok(jwtToken.getAccessToken());
        } else {
            return ResponseEntity.ok("unauthorized");   // TODO 로그인 실패 처리
        }
    }

    @Operation(
            summary = "회원가입",
            description = "회원정보를 통해 회원가입을 진행합니다. 회원가입 후 회원 정보와 프로필 이미지를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원가입 성공. 회원 정보 및 프로필 이미지를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "회원가입 실패. 잘못된 요청 또는 파일 저장 실패."
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpDto signUpDto) throws IOException {
        MemberDto memberDto = memberService.signUp(signUpDto);
        UploadFile uploadFile = fileStore.storeFile(signUpDto.getProfileImg());
        if (uploadFile != null) {
            memberService.changeProfileImg(memberDto.getUsername(), uploadFile);
        }
        return ResponseEntity.ok(memberDto);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 진행합니다. 현재 사용자의 엑세스 토큰을 무효화하고 인증 정보를 제거합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공. 'success' 반환."
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("access_token", "")
                .path("/")
                .httpOnly(true)
                .value(null)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("success");
    }

    @Operation(
            summary = "회원 정보 조회",
            description = "username을 통해 username, nickname, profileImg를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 정보 조회 성공. username, nickname, profileImg 정보 반환."
    )
    @ApiResponse(
            responseCode = "404",
            description = "회원 정보 조회 실패. 사용자가 존재하지 않음."
    )
    @GetMapping("/{username}")
    public ResponseEntity<?> singleStoryWriteForm(@PathVariable String username) {
        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(username);
        return ResponseEntity.ok(memberSimpleDto);
    }
}
