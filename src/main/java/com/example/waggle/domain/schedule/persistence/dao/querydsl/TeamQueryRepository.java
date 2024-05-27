package com.example.waggle.domain.schedule.persistence.dao.querydsl;

public interface TeamQueryRepository {
    void deleteTeamWithRelationsByMemberId(Long memberId);

    void deleteTeamWithRelations(Long teamId);
}
