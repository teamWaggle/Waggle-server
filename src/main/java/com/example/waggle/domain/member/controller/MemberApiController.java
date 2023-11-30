package com.example.waggle.domain.member.controller;

import com.example.waggle.global.component.file.FileStore;
import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.security.JwtToken;
import com.example.waggle.domain.member.dto.MemberSummaryDto;
import com.example.waggle.domain.member.dto.SignInDto;
import com.example.waggle.domain.member.dto.SignUpDto;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
@Tag(name = "Member API", description = "회원 API")
public class MemberApiController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final FileStore fileStore;

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 통해 로그인을 진행합니다. 성공 시 엑세스 토큰을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공. 엑세스 토큰을 반환합니다.")
    @ApiResponse(responseCode = "401", description = "로그인 실패. 인증되지 않음.")
    @PostMapping("/tokens")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        JwtToken jwtToken = memberCommandService.signIn(signInDto);

        if (jwtToken != null) {
            Cookie cookie = new Cookie("access_token", jwtToken.getAccessToken());
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            return ResponseEntity.ok(jwtToken.getAccessToken());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        }
    }

    @Operation(summary = "회원가입", description = "회원정보를 통해 회원가입을 진행합니다. 회원가입 후 회원 정보와 프로필 이미지를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공. 회원 정보 및 프로필 이미지를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "회원가입 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PostMapping
    public ResponseEntity<?> register(@RequestPart SignUpDto signUpDto,
                                      @RequestPart(value = "profileImg", required = false) MultipartFile profileImg)
            throws IOException {
        try {
            UploadFile uploadFile = fileStore.storeFile(profileImg);
            MemberSummaryDto memberSummaryDto = memberCommandService.signUp(signUpDto, uploadFile);
            return ResponseEntity.ok(memberSummaryDto);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다. 현재 사용자의 엑세스 토큰을 무효화하고 인증 정보를 제거합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공. 'success' 반환.")
    @DeleteMapping("/tokens")
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

    @Operation(summary = "회원 정보 조회", description = "username을 통해 username, nickname, profileImg를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공. username, nickname, profileImg 정보 반환.")
    @ApiResponse(responseCode = "404", description = "회원 정보 조회 실패. 사용자가 존재하지 않음.")
    @GetMapping("/{username}")
    public ResponseEntity<?> getMemberInfo(@PathVariable String username) {
        MemberSummaryDto memberSummaryDto = memberQueryService.getMemberSummaryDto(username);
        return ResponseEntity.ok(memberSummaryDto);
    }
}
