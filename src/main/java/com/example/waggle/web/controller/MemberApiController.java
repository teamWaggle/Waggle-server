package com.example.waggle.web.controller;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.domain.member.service.EmailService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberProfileDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberUpdateDto;
import com.example.waggle.web.dto.member.MemberResponse;
import com.example.waggle.web.dto.member.MemberResponse.MemberDetailDto;
import com.example.waggle.web.dto.member.MemberResponse.MemberMentionListDto;
import com.example.waggle.web.dto.member.VerifyMailRequest.EmailSendDto;
import com.example.waggle.web.dto.member.VerifyMailRequest.EmailVerificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Member API", description = "회원 API")
public class MemberApiController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final EmailService emailService;


    @Operation(summary = "회원가입", description = "회원정보를 통해 회원가입을 진행합니다. 임의의 userUrl과 nickname을 넣어줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping
    public ApiResponseDto<Long> signUp(@RequestBody MemberCredentialsDto registerMemberRequest) {
        Long memberId = memberCommandService.signUp(registerMemberRequest);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 등록 🔑", description = "회원가입 후 회원정보를 처음 등록합니다. 서버에서 임의로 설정해둔 정보들을 수정합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/info")
    public ApiResponseDto<Long> initializeMemberProfile(
            @RequestBody MemberProfileDto memberProfileRequest,
            @AuthUser Member member) {
        Long memberId = memberCommandService.initializeMemberProfile(memberProfileRequest, member);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 수정 🔑", description = "회원정보를 수정합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping
    public ApiResponseDto<Long> updateInfo(@RequestBody MemberUpdateDto updateMemberRequest,
                                           @AuthUser Member member) {
        Long memberId = memberCommandService.updateMemberProfile(updateMemberRequest, member);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 조회", description = "memberId를 통해 userUrl, nickname, profileImg를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{memberId}")
    public ApiResponseDto<MemberDetailDto> getMemberInfo(@PathVariable("memberId") Long memberId) {
        Member member = memberQueryService.getMemberById(memberId);
        return ApiResponseDto.onSuccess(MemberConverter.toMemberDetailDto(member));
    }

    @Operation(summary = "회원 검색", description = "nickname 일부 혹은 전체를 검색하여 검색어에 해당하는 모든 회원을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/by-nickname/{nickname}")
    public ApiResponseDto<MemberMentionListDto> searchMembersByNicknameContaining(@PathVariable("nickname") String nickname) {
        List<Member> members = memberQueryService.getMembersByNicknameContaining(nickname);
        return ApiResponseDto.onSuccess(MemberConverter.toMentionListDto(members));
    }

    @Operation(summary = "이메일 전송", description = "사용자에게 인증 메일을 전송합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/email/send")
    public ApiResponseDto<Boolean> sendMail(@RequestBody @Validated EmailSendDto sendEmailRequest) {
        emailService.sendMail(sendEmailRequest.getEmail(), "email");
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "이메일 인증", description = "받은 이메일을 통해 사용자의 이메일 인증을 진행합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/email/verify")
    public ApiResponseDto<Boolean> verifyMail(@RequestBody EmailVerificationDto verifyEmailRequest) {
        memberCommandService.verifyMail(verifyEmailRequest);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "비밀번호 변경 시 이메일 인증", description = "비밀번호 변경 전 이메일 인증을 시도합니다. 결과값으로 member의 id를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/email/verify/password")
    public ApiResponseDto<Long> verifyMailForPasswordChanging(
            @RequestBody EmailVerificationDto emailVerificationRequest) {
        Long memberId = memberCommandService.verifyEmailForPasswordChange(emailVerificationRequest);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "비밀번호 변경", description = "앞서 받은 멤버아이디와 일치하는 회원의 비밀번호를 변경합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/{memberId}/password")
    public ApiResponseDto<Long> verifyMailForPasswordChanging(@PathVariable("memberId") Long memberId,
                                                              @RequestBody MemberRequest.PasswordDto updatePasswordRequest) {
        memberCommandService.updatePassword(memberId, updatePasswordRequest.getPassword());
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "이메일 찾기", description = "성명과 생년월일을 통해 이메일을 찾습니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/email/find")
    public ApiResponseDto<MemberResponse.EmailListDto> findEmail(@RequestParam("name") String name,
                                                                 @RequestParam("birthday") LocalDate birthday) {
        List<Member> members = memberQueryService.getMembersByNameAndBirthday(name, birthday);
        return ApiResponseDto.onSuccess(MemberConverter.toEmailListDto(members));
    }

    @Operation(summary = "이메일 중복 검사", description = "제공된 이메일이 이미 사용 중인지 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/check-email")
    public ApiResponseDto<Boolean> checkEmail(@RequestParam("email") String email) {
        memberQueryService.validateEmailDuplication(email);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "회원 URL 중복 검사", description = "제공된 회원 URL이 이미 사용 중인지 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/check-user-url")
    public ApiResponseDto<Boolean> checkUsername(@RequestParam("userUrl") String userUrl) {
        memberQueryService.validateUserUrlDuplication(userUrl);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "닉네임 중복 검사", description = "제공된 닉네임이 이미 사용 중인지 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/check-nickname")
    public ApiResponseDto<Boolean> checkNickname(@RequestParam("nickname") String nickname) {
        memberQueryService.validateNicknameDuplication(nickname);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "회원 삭제", description = "특정 회원을 삭제합니다. 회원과 관련된 데이터가 모두 삭제됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{memberId}")
    public ApiResponseDto<Boolean> deleteMember(@PathVariable("memberId") Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "🔑 휴면계정 등록", description = "특정 회원을 휴면계정으로 전환합니다. 하루동안 휴면계정을 풀지 않으면 회원관련 정보가 모두 삭제됩니다")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/role/dormant")
    public ApiResponseDto<Boolean> convertDormant(@AuthUser Member member) {
        memberCommandService.convertRole(member, Role.DORMANT);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "🔑 휴면계정 해제", description = "특정 회원을 휴면계정에서 일반회원으로 전환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/role/user")
    public ApiResponseDto<Boolean> convertUser(@AuthUser Member member) {
        memberCommandService.convertRole(member, Role.USER);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "🔑 회원 강제 삭제", description = "특정 회원을 관리자가 강제 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{memberId}/force")
    public ApiResponseDto<Boolean> deleteMemberForce(@AuthUser Member admin, @PathVariable Long memberId) {
        memberCommandService.deleteMemberAsAdmin(admin, memberId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

}
