package com.example.waggle.service.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public MemberDto signIn(SignInDto signInDto) {
        return null;
    }

    @Transactional
    @Override
    public MemberDto signUp(SignUpDto signUpDto) {
        return MemberDto.toDto(memberRepository.save(signUpDto.toEntity()));
    }

    @Override
    public void signOut(HttpSession session) {

    }

    @Override
    public List<PetDto> findPets(String username) {
        return null;
    }

    @Override
    public List<TeamDto> findTeams(String username) {
        return null;
    }

    @Override
    public List<StoryDto> findStories(String username) {
        return null;
    }

    @Override
    public List<QuestionDto> findQuestions(String username) {
        return null;
    }

    @Override
    public List<SosDto> findSoss(String username) {
        return null;
    }
}
