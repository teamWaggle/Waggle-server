package com.example.waggle.service.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.MemberRepository;
import com.example.waggle.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    public Optional<PetDto> findByPetId(Long petId) {
        Optional<Pet> findPet = petRepository.findById(petId);
        return findPet.map(PetDto::toDto);
    }

    List<PetDto> findByMemberId(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElse(null);
        return findMember.getPets().stream().map(PetDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public PetDto addPet(PetDto petDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        // DB에 저장
        Pet pet = petRepository.save(petDto.toEntity());
        // 연관관계 설정
        pet.setMember(member);
        return PetDto.toDto(pet);
    }

    @Transactional
    public PetDto updatePet(Long petId, PetDto petDto) {
        Optional<Pet> findPet = petRepository.findById(petId);
        if (findPet.isPresent()) {
            Pet pet = findPet.get();

            Pet updatedPet = Pet.builder()
                    .id(pet.getId())
                    .name(petDto.getName())
                    .breed(petDto.getBreed())
                    .sex(petDto.getSex())
                    .birthday(petDto.getBirthday())
                    .profileImg(petDto.getProfileImg())
                    .build();

            // 업데이트된 펫을 저장합니다
            Pet savedPet = petRepository.save(updatedPet);

            PetDto updatedPetDto = PetDto.toDto(savedPet);

            return updatedPetDto;
        } else {
            // 존재하지 않는 펫 처리
            return null;
        }
    }

    @Transactional
    public Boolean removePet(Long petId) {
        Optional<Pet> removalPet = petRepository.findById(petId);
        if(removalPet.isPresent()) {
            Pet pet = removalPet.get();
            Member member = pet.getMember();
            member.getPets().remove(pet);
            petRepository.delete(pet);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
