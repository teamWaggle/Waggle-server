package com.example.waggle.member.service;

import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.commons.security.JwtToken;
import com.example.waggle.commons.security.JwtTokenProvider;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.dto.MemberDetailDto;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.dto.SignInDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.pet.domain.Pet;
import com.example.waggle.pet.dto.PetDto;
import com.example.waggle.pet.repository.PetRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.ALREADY_USING_USERNAME;
import static com.example.waggle.commons.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private static final String DEFAULT_ROLE = "USER";

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public JwtToken signIn(SignInDto signInDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

    @Transactional
    @Override
    public MemberDetailDto signUp(SignUpDto signUpDto, UploadFile profileImg) {
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new CustomAlertException(ALREADY_USING_USERNAME);
        }
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        List<String> roles = new ArrayList<>();
        roles.add(DEFAULT_ROLE);

        Member savedMember = memberRepository.save(signUpDto.toEntity(encodedPassword, roles));
        updateProfileImg(savedMember.getUsername(), profileImg);


        List<PetDto> petDtos = signUpDto.getPets();
        List<Pet> pets = petDtos.stream().map(petDto -> petDto.toEntity(savedMember)).collect(Collectors.toList());
        for (Pet pet : pets) {
            petRepository.save(pet);
        }
        savedMember.setPets(pets);

        return MemberDetailDto.toDto(savedMember);
    }

    @Override
    public void signOut(HttpSession session) {

    }

    @Override
    public MemberSummaryDto getMemberSummaryDto(String username) {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));
        return MemberSummaryDto.toDto(findMember);
    }

    @Override
    @Transactional
    public MemberDetailDto updateProfileImg(String username, UploadFile profileImg) {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));
        findMember.changeProfileImg(profileImg);
        return MemberDetailDto.toDto(findMember);
    }
}
