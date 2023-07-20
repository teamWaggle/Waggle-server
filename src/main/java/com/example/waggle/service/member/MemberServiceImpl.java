package com.example.waggle.service.member;

import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.component.jwt.JwtTokenProvider;
import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.repository.member.PetRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public JwtToken signIn(SignInDto signInDto) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional
    @Override
    public MemberDto signUp(SignUpDto signUpDto) {
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        // USER 권한 부여
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        // Member save
        Member savedMember = memberRepository.save(signUpDto.toEntity(encodedPassword, roles));

        // Pet save

        List<PetDto> petDtos = signUpDto.getPets();
        List<Pet> pets = petDtos.stream().map(petDto -> petDto.toEntity(savedMember)).collect(Collectors.toList()); // PetDto -> Pet mapping
        for (Pet pet : pets) {
            petRepository.save(pet);
        }
        savedMember.setPets(pets);


        return MemberDto.toDto(savedMember);
    }

    @Override
    public void signOut(HttpSession session) {

    }

    @Override
    public MemberSimpleDto findMemberSimpleDto(String username) {
        Member findMember = memberRepository.findByUsername(username).orElse(null);
        return MemberSimpleDto.toDto(findMember);
    }

    @Override
    @Transactional
    public MemberDto changeProfileImg(String username, UploadFile profileImg) {
        Member findMember = memberRepository.findByUsername(username).orElse(null);
        findMember.changeProfileImg(profileImg);
        return MemberDto.toDto(findMember);
    }
}
