package com.example.waggle.domain.member.persistence.dao.querydsl;

public interface MemberQueryRepository {
    void deleteMemberWithRelations(Long memberId);
}
