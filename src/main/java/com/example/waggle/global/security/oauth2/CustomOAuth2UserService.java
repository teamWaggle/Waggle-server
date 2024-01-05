package com.example.waggle.global.security.oauth2;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.security.CustomUserDetails;
import com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider;
import com.example.waggle.global.security.oauth2.info.OAuth2UserInfo;
import com.example.waggle.global.util.NameUtil;
import com.example.waggle.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        //OAuth2 로그인 플랫폼 구분
        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        Optional<Member> byEmail = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        Member member = byEmail.orElseGet(() -> registerMember(authProvider, oAuth2UserInfo));
        //이미 가입된 경우
        //많은 프로젝트 대부분이 플랫폼의 정보를 업데이트 했지만 우리 프로젝트에서는 단순히 '권한'만 발급받을 것

        return CustomUserDetails.create(member);
    }

    private Member registerMember(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        Member register = Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .username(oAuth2UserInfo.getOAuth2Id())
                .password(PasswordUtil.generateRandomPassword(10))
                .nickname(NameUtil.generateAutoNickname())
                .authProvider(authProvider)
                .role(Role.GUEST)           //회원가입시에만 guest로 두고 이후 사용에는 user로 돌린다
                .build();

        return memberRepository.save(register);
    }
}