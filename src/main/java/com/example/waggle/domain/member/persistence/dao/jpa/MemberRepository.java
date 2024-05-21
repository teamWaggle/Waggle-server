package com.example.waggle.domain.member.persistence.dao.jpa;

import com.example.waggle.domain.member.persistence.dao.querydsl.MemberQueryRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByUserUrl(String userUrl);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    List<Member> findByNameAndBirthday(String name, LocalDate birthday);

    List<Member> findByNicknameContaining(String nickname);

    List<Member> findByRole(Role role);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByUserUrl(String userUrl);
}
