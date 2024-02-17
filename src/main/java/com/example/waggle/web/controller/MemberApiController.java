package com.example.waggle.web.controller;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.EmailService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.security.annotation.AuthUser;
import com.example.waggle.global.util.MediaUtil;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
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


    @Operation(summary = "회원가입", description = "회원정보를 통해 회원가입을 진행합니다. 임의의 userUrl과 nickname을 넣어줍니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공. 회원 정보 및 프로필 이미지를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "회원가입 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PostMapping
    public ApiResponseDto<Long> signUp(@RequestBody MemberRequest.AccessDto request) {
        Long memberId = memberCommandService.signUp(request);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 등록", description = "회원가입 후 회원정보를 처음 등록합니다. 서버에서 임의로 설정해둔 정보들을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "회원정보 등록 성공. 회원 정보 및 프로필 이미지를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "회원정보 등록 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PutMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> registerInfo(@AuthUser UserDetails userDetails,
                                             @RequestPart MemberRequest.RegisterDto request,
                                             @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        request.setProfileImgUrl(MediaUtil.saveProfileImg(multipartFile, awsS3Service));
        Long memberId = memberCommandService.registerMemberInfo(userDetails.getUsername(), request);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 수정", description = "회원정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "회원정보 수정 성공.")
    @ApiResponse(responseCode = "400", description = "회원정보 수정 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateInfo(
            @RequestPart MemberRequest.Put request,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg,
            @RequestParam boolean allowUpload,
            @AuthUser UserDetails userDetails) {
        String removePrefixCoverUrl = MediaUtil.removePrefix(request.getProfileImgUrl());
        if (allowUpload) {
            awsS3Service.deleteFile(removePrefixCoverUrl);
            request.setProfileImgUrl(MediaUtil.saveProfileImg(profileImg, awsS3Service));
        } else {
            request.setProfileImgUrl(removePrefixCoverUrl);
        }
        Long memberId = memberCommandService.updateMemberInfo(userDetails.getUsername(), request);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 조회", description = "username을 통해 username, nickname, profileImg를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공. username, nickname, profileImg 정보 반환.")
    @ApiResponse(responseCode = "404", description = "회원 정보 조회 실패. 사용자가 존재하지 않음.")
    @GetMapping("/{username}")
    public ApiResponseDto<MemberResponse.DetailDto> getMemberInfo(@PathVariable String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        return ApiResponseDto.onSuccess(MemberConverter.toMemberDetailDto(member));
    }

    @Operation(summary = "이메일 전송", description = "사용자에게 인증 메일을 전송합니다.")
    @ApiResponse(responseCode = "200", description = "이메일 전송 성공.")
    @ApiResponse(responseCode = "400", description = "이메일 전송 실패. 잘못된 이메일 형식 등.")
    @PostMapping("/email/send")
    public ApiResponseDto<Boolean> sendMail(@RequestBody @Valid String email) {
        emailService.sendMail(email, "email");
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "이메일 인증", description = "받은 이메일을 통해 사용자의 이메일 인증을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "이메일 인증 성공.")
    @ApiResponse(responseCode = "400", description = "이메일 인증 실패. 잘못된 인증 정보 등.")
    @PostMapping("/email/verify")
    public ApiResponseDto<Boolean> verifyMail(@RequestBody VerifyMailRequest verifyMailRequest) {
        memberCommandService.verifyMail(verifyMailRequest);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "이메일 중복 검사", description = "제공된 이메일이 이미 사용 중인지 확인합니다.")
    @ApiResponse(responseCode = "200", description = "중복 검사 결과 반환")
    @GetMapping("/check-email")
    public ApiResponseDto<Boolean> checkEmail(@RequestParam String email) {
        memberQueryService.validateEmailDuplication(email);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "유저네임 중복 검사", description = "제공된 유저네임이 이미 사용 중인지 확인합니다.")
    @ApiResponse(responseCode = "200", description = "중복 검사 결과 반환")
    @GetMapping("/check-username")
    public ApiResponseDto<Boolean> checkUsername(@RequestParam String username) {
        memberQueryService.validateUsernameDuplication(username);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "닉네임 중복 검사", description = "제공된 닉네임이 이미 사용 중인지 확인합니다.")
    @ApiResponse(responseCode = "200", description = "중복 검사 결과 반환")
    @GetMapping("/check-nickname")
    public ApiResponseDto<Boolean> checkNickname(@RequestParam String nickname) {
        memberQueryService.validateNicknameDuplication(nickname);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "인증 회원 삭제", description = "로그인 된 특정 회원을 삭제합니다. 회원이 작성한 관련된 게시글, 댓글 등이 모두 삭제됩니다.")
    @ApiResponse(responseCode = "200", description = "멤버 삭제 성공.")
    @DeleteMapping
    public ApiResponseDto<Boolean> deleteMember() {
        memberCommandService.deleteMember();
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
