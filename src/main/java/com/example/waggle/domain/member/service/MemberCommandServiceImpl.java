package com.example.waggle.domain.member.service;

import static com.example.waggle.global.exception.ErrorCode.ALREADY_USING_USERNAME;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.global.exception.CustomApiException;
import com.example.waggle.global.security.JwtToken;
import com.example.waggle.global.security.JwtTokenProvider;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.pet.PetDto;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandServiceImpl implements MemberCommandService {

    private static final String DEFAULT_ROLE = "USER";

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtToken signIn(MemberRequest.LoginRequestDto request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

    @Override
    public Member signUp(MemberRequest.RegisterRequestDto request, String profileImgUrl) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new CustomApiException(ALREADY_USING_USERNAME);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        List<String> roles = new ArrayList<>();
        roles.add(DEFAULT_ROLE);

        Member createdMember = Member.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .address(request.getAddress())
                .phone(request.getPhone())
                .roles(roles)
                .profileImgUrl(profileImgUrl)
                .build();

        Member member = memberRepository.save(createdMember);

        List<PetDto> petDtos = request.getPets();
        List<Pet> pets = petDtos.stream().map(petDto -> petDto.toEntity(member)).collect(Collectors.toList());
        for (Pet pet : pets) {
            petRepository.save(pet);
        }
        member.setPets(pets);
        return member;
    }

    @Override
    public void signOut(HttpSession session) {

    }

}
