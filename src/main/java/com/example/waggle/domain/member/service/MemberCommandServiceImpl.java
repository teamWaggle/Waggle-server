package com.example.waggle.domain.member.service;

import static com.example.waggle.global.exception.ErrorCode.ALREADY_USING_USERNAME;
import static com.example.waggle.global.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.exception.CustomApiException;
import com.example.waggle.global.security.JwtToken;
import com.example.waggle.global.security.JwtTokenProvider;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.web.dto.member.MemberSummaryDto;
import com.example.waggle.web.dto.member.SignInDto;
import com.example.waggle.web.dto.member.SignUpDto;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.pet.domain.Pet;
import com.example.waggle.web.dto.pet.PetDto;
import com.example.waggle.domain.pet.repository.PetRepository;
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
    public JwtToken signIn(SignInDto signInDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

    @Override
    public MemberSummaryDto signUp(SignUpDto signUpDto, UploadFile profileImg) {
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new CustomApiException(ALREADY_USING_USERNAME);
        }
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        List<String> roles = new ArrayList<>();
        roles.add(DEFAULT_ROLE);

        Member member = memberRepository.save(signUpDto.toEntity(encodedPassword, roles));
        updateProfileImg(member.getUsername(), profileImg);

        List<PetDto> petDtos = signUpDto.getPets();
        List<Pet> pets = petDtos.stream().map(petDto -> petDto.toEntity(member)).collect(Collectors.toList());
        for (Pet pet : pets) {
            petRepository.save(pet);
        }
        member.setPets(pets);
        return MemberSummaryDto.toDto(member);
    }

    @Override
    public void signOut(HttpSession session) {

    }

    @Override
    public Long updateProfileImg(String username, UploadFile profileImg) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException(MEMBER_NOT_FOUND));
        member.changeProfileImg(profileImg);
        return member.getId();
    }
}
