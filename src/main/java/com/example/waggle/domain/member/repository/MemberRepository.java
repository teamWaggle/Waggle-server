package com.example.waggle.domain.member.repository;

import com.example.waggle.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    List<Member> findByNameAndBirthday(String name, LocalDate birthday);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByUserUrl(String userUrl);
}
