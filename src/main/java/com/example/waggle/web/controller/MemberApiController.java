package com.example.waggle.web.controller;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.EmailService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.MemberResponse;
import com.example.waggle.web.dto.member.VerifyMailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final AwsS3Service awsS3Service;
    private final EmailService emailService;


    @Operation(summary = "회원가입", description = "회원정보를 통해 회원가입을 진행합니다. 회원가입 후 회원 정보와 프로필 이미지를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공. 회원 정보 및 프로필 이미지를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "회원가입 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PostMapping
    public ApiResponseDto<Long> register(
            @RequestPart MemberRequest.RegisterRequestDto request,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        String profileImgUrl = null;
        if (profileImg != null) {
            profileImgUrl = awsS3Service.uploadFile(profileImg);
        }
        Long memberId = memberCommandService.signUp(request);
        return ApiResponseDto.onSuccess(memberId);
    }
    @Operation(summary = "회원 정보 수정", description = "회원정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "회원정보 수정 성공.")
    @ApiResponse(responseCode = "400", description = "회원정보 수정 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PutMapping
    public ApiResponseDto<Long> updateInfo(
            @RequestPart MemberRequest.PutDto request,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        String profileImgUrl = null;
        if (profileImg != null) {
            profileImgUrl = awsS3Service.uploadFile(profileImg);
        }
        Long memberId = memberCommandService.updateMemberInfo(request);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 조회", description = "username을 통해 username, nickname, profileImg를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공. username, nickname, profileImg 정보 반환.")
    @ApiResponse(responseCode = "404", description = "회원 정보 조회 실패. 사용자가 존재하지 않음.")
    @GetMapping("/{username}")
    public ApiResponseDto<MemberResponse.MemberDetailDto> getMemberInfo(@PathVariable String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        return ApiResponseDto.onSuccess(MemberConverter.toMemberDetailDto(member));
    }

    @PostMapping("/send-mail")
    public ApiResponseDto<Boolean> sendMail(@RequestBody @Valid String email) {
        emailService.sendMail(email, "email");
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @PostMapping("/verify/mail")
    public ApiResponseDto<Boolean> verifyMail(@RequestBody VerifyMailRequest verifyMailRequest) {
        memberCommandService.verifyMail(verifyMailRequest);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
