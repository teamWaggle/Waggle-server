package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.pet.PetDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member signUp(MemberRequest.RegisterRequestDto request, String profileImgUrl) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new MemberHandler(ErrorStatus.ALREADY_USING_USERNAME);
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

}
