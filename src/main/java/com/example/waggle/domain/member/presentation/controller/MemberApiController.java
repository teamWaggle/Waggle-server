package com.example.waggle.domain.member.presentation.controller;

import com.example.waggle.domain.chat.application.ChatRoomQueryService;
import com.example.waggle.domain.follow.application.FollowQueryService;
import com.example.waggle.domain.member.application.MemberCommandService;
import com.example.waggle.domain.member.application.MemberQueryService;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberCredentialsDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberProfileDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberUpdateDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.PasswordDto;
import com.example.waggle.domain.member.presentation.dto.MemberResponse;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberDetailDto;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberMentionListDto;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
import com.example.waggle.domain.member.presentation.dto.VerifyMailRequest.EmailSendDto;
import com.example.waggle.domain.member.presentation.dto.VerifyMailRequest.EmailVerificationDto;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.service.email.EmailService;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.ADMIN;
import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.AUTH;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Member API", description = "회원 API")
public class MemberApiController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final FollowQueryService followQueryService;
    private final ChatRoomQueryService chatRoomQueryService;
    private final EmailService emailService;


    @Operation(summary = "회원가입", description = "회원정보를 통해 회원가입을 진행합니다. 임의의 userUrl과 nickname을 넣어줍니다.")
    @ApiErrorCodeExample
    @PostMapping
    public ApiResponseDto<Long> signUp(@RequestBody @Valid MemberCredentialsDto registerMemberRequest) {
        Long memberId = memberCommandService.signUp(registerMemberRequest);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 등록 🔑", description = "회원가입 후 회원정보를 처음 등록합니다. 서버에서 임의로 설정해둔 정보들을 수정합니다.")
    @ApiErrorCodeExample(status = AUTH)
    @PutMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> initializeMemberProfile(
            @RequestPart("memberProfileRequest") @Valid MemberProfileDto memberProfileRequest,
            @AuthUser Member member) {
        memberProfileRequest.setMemberProfileImg(MediaUtil.removePrefix(memberProfileRequest.getMemberProfileImg()));
        Long memberId = memberCommandService.initializeMemberProfile(memberProfileRequest, member);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 수정 🔑", description = "회원정보를 수정합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEDIA_REQUEST_IS_EMPTY
    }, status = AUTH)
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateInfo(
            @RequestPart("updateMemberRequest") @Valid MemberUpdateDto updateMemberRequest,
            @AuthUser Member member) {
        updateMemberRequest.setMemberProfileImg(MediaUtil.removePrefix(updateMemberRequest.getMemberProfileImg()));
        Long memberId = memberCommandService.updateMemberProfile(updateMemberRequest, member);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "회원 정보 조회", description = "userUrl을 통해 userUrl, nickname, profileImg를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @GetMapping("/{userUrl}")
    public ApiResponseDto<MemberDetailDto> getMemberInfo(@PathVariable("userUrl") String userUrl) {
        Member member = memberQueryService.getMemberByUserUrl(userUrl);
        MemberDetailDto memberDetailDto = MemberConverter.toMemberDetailDto(member);
        memberDetailDto.setFollowerCount(followQueryService.getFollowersByUserUrl(userUrl).stream().count());
        memberDetailDto.setFollowingCount(followQueryService.getFollowingsByUserUrl(userUrl).stream().count());
        memberDetailDto.setChatRoomCount(chatRoomQueryService.countChatRoomsByMemberId(member.getId()));
        return ApiResponseDto.onSuccess(memberDetailDto);
    }

    @Operation(summary = "프론트 저장용 회원 정보 조회 🔑", description = "Access Token을 통해 memberId, userUrl을 조회합니다.")
    @ApiErrorCodeExample(status = AUTH)
    @GetMapping("/info")
    public ApiResponseDto<MemberSummaryDto> getMemberInfoByAuth(@AuthUser Member member) {
        return ApiResponseDto.onSuccess(MemberConverter.toMemberSummaryDto(member));
    }

    @Operation(summary = "회원 검색", description = "nickname 일부 혹은 전체를 검색하여 검색어에 해당하는 모든 회원을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.SEARCHING_KEYWORD_IS_TOO_SHORT
    })
    @GetMapping("/by-nickname/{nickname}")
    public ApiResponseDto<MemberMentionListDto> searchMembersByNicknameContaining(
            @PathVariable("nickname") String nickname) {
        ObjectUtil.validateKeywordLength(nickname);
        List<Member> members = memberQueryService.getMembersByNicknameContaining(nickname);
        return ApiResponseDto.onSuccess(MemberConverter.toMentionListDto(members));
    }

    @Operation(summary = "이메일 전송", description = "사용자에게 인증 메일을 전송합니다.")
    @ApiErrorCodeExample
    @PostMapping("/email/send")
    public ApiResponseDto<Boolean> sendMail(@RequestBody @Valid EmailSendDto sendEmailRequest) {
        emailService.sendMail(sendEmailRequest.getEmail(), "email");
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "이메일 인증", description = "받은 이메일을 통해 사용자의 이메일 인증을 진행합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @PostMapping("/email/verify")
    public ApiResponseDto<Boolean> verifyMail(@RequestBody @Valid EmailVerificationDto verifyEmailRequest) {
        memberCommandService.verifyMail(verifyEmailRequest);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "비밀번호 변경 시 이메일 인증", description = "비밀번호 변경 전 이메일 인증을 시도합니다. 결과값으로 member의 id를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @PostMapping("/email/verify/password")
    public ApiResponseDto<Long> verifyMailForPasswordChanging(
            @RequestBody @Valid EmailVerificationDto emailVerificationRequest) {
        Long memberId = memberCommandService.verifyEmailForPasswordChange(emailVerificationRequest);
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "비밀번호 변경", description = "앞서 받은 멤버아이디와 일치하는 회원의 비밀번호를 변경합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @PutMapping("/{memberId}/password")
    public ApiResponseDto<Long> verifyMailForPasswordChanging(@PathVariable("memberId") Long memberId,
                                                              @RequestBody @Valid PasswordDto updatePasswordRequest) {
        memberCommandService.updatePassword(memberId, updatePasswordRequest.getPassword());
        return ApiResponseDto.onSuccess(memberId);
    }

    @Operation(summary = "이메일 찾기", description = "성명과 생년월일을 통해 이메일을 찾습니다.")
    @ApiErrorCodeExample
    @GetMapping("/email/find")
    public ApiResponseDto<MemberResponse.EmailListDto> findEmail(@RequestParam("name") String name,
                                                                 @RequestParam("birthday") LocalDate birthday) {
        List<Member> members = memberQueryService.getMembersByNameAndBirthday(name, birthday);
        return ApiResponseDto.onSuccess(MemberConverter.toEmailListDto(members));
    }

    @Operation(summary = "이메일 중복 검사", description = "제공된 이메일이 이미 사용 중인지 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_DUPLICATE_EMAIL
    })
    @GetMapping("/check-email")
    public ApiResponseDto<Boolean> checkEmail(@RequestParam("email") String email) {
        memberQueryService.validateEmailDuplication(email);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "회원 URL 중복 검사", description = "제공된 회원 URL이 이미 사용 중인지 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_DUPLICATE_USER_URL
    })
    @GetMapping("/check-user-url")
    public ApiResponseDto<Boolean> checkUsername(@RequestParam("userUrl") String userUrl) {
        memberQueryService.validateUserUrlDuplication(userUrl);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "닉네임 중복 검사", description = "제공된 닉네임이 이미 사용 중인지 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_DUPLICATE_NICKNAME
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

    @Operation(summary = "휴면계정 등록 🔑", description = "특정 회원을 휴면계정으로 전환합니다. 하루동안 휴면계정을 풀지 않으면 회원관련 정보가 모두 삭제됩니다")
    @ApiErrorCodeExample(status = AUTH)
    @PutMapping("/role/dormant")
    public ApiResponseDto<Boolean> convertDormant(@AuthUser Member member) {
        memberCommandService.convertRole(member, Role.DORMANT);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "휴면계정 해제 🔑", description = "특정 회원을 휴면계정에서 일반회원으로 전환합니다.")
    @ApiErrorCodeExample(status = AUTH)
    @PutMapping("/role/user")
    public ApiResponseDto<Boolean> convertUser(@AuthUser Member member) {
        memberCommandService.convertRole(member, Role.USER);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "비밀번호 재설정 🔑", description = "인증된 회원의 비밀번호를 재설정합니다.")
    @ApiErrorCodeExample(status = AUTH)
    @PutMapping("/password")
    public ApiResponseDto<Long> updatePassword(@AuthUser Member member,
                                               @RequestBody @Valid PasswordDto updatePasswordRequest) {
        memberCommandService.updatePassword(member.getId(), updatePasswordRequest.getPassword());
        return ApiResponseDto.onSuccess(member.getId());
    }

    @Operation(summary = "회원 강제 삭제 🔑", description = "특정 회원을 관리자가 강제 삭제합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEDIA_REQUEST_IS_EMPTY
    }, status = ADMIN)
    @DeleteMapping("/{memberId}/admin")
    public ApiResponseDto<Boolean> deleteMemberForce(@PathVariable Long memberId,
                                                     @AuthUser Member admin) {
        memberCommandService.deleteMemberByAdmin(admin, memberId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

}
