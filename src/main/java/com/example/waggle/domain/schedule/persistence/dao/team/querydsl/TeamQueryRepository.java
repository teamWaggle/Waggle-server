package com.example.waggle.domain.schedule.persistence.dao.team.querydsl;

public interface TeamQueryRepository {
    void deleteTeamWithRelationsByMemberId(Long memberId);
}
