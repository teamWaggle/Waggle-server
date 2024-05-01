package com.example.waggle.domain.schedule.repository;

import static com.example.waggle.domain.member.entity.QMember.member;
import static com.example.waggle.domain.schedule.entity.QMemberSchedule.memberSchedule;
import static com.example.waggle.domain.schedule.entity.QSchedule.schedule;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberScheduleQueryRepositoryImpl implements MemberScheduleQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Long countOverlappedSchedule(Member user, Schedule findSchedule) {
        return query.select(memberSchedule.count())
                .from(memberSchedule)
                .leftJoin(memberSchedule.member, member)
                .leftJoin(memberSchedule.schedule, schedule)
                .where(member.eq(user)
                        .and(schedule.id.ne(findSchedule.getId()))
                        .and(getOverlappingSchedule(
                                findSchedule.getStartDate(),
                                findSchedule.getStartTime(),
                                findSchedule.getEndDate(),
                                findSchedule.getEndTime())))
                .fetchFirst();
    }

    @Override
    public List<Schedule> findOverlappingScheduleList(Member user, Schedule findSchedule) {
        return query.select(schedule)
                .from(memberSchedule)
                .leftJoin(memberSchedule.member, member)
                .leftJoin(memberSchedule.schedule, schedule)
                .where(member.eq(user)
                        .and(schedule.id.ne(findSchedule.getId()))
                        .and(getOverlappingSchedule(
                                findSchedule.getStartDate(),
                                findSchedule.getStartTime(),
                                findSchedule.getEndDate(),
                                findSchedule.getEndTime())))
                .fetch();
    }

    private BooleanExpression getOverlappingSchedule(LocalDate startDate,
                                                     LocalTime startTime,
                                                     LocalDate endDate,
                                                     LocalTime endTime) {
        LocalDateTime targetStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime targetEnd = LocalDateTime.of(endDate, endTime);
        return startDateTime.before(targetEnd).and(endDateTime.after(targetStart));
    }

    DateTimeExpression<LocalDateTime> startDateTime = Expressions.dateTimeTemplate(
            LocalDateTime.class,
            "STR_TO_DATE(CONCAT({0}, ' ', {1}), '%Y-%m-%d %H:%i')",
            schedule.startDate,
            schedule.startTime
    );

    DateTimeExpression<LocalDateTime> endDateTime = Expressions.dateTimeTemplate(
            LocalDateTime.class,
            "STR_TO_DATE(CONCAT({0}, ' ', {1}), '%Y-%m-%d %H:%i')",
            schedule.endDate,
            schedule.endTime
    );

}
