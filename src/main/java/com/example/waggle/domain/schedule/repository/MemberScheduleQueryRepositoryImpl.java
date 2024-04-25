package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.member.entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.waggle.domain.member.entity.QMember.member;
import static com.example.waggle.domain.schedule.entity.QMemberSchedule.memberSchedule;
import static com.example.waggle.domain.schedule.entity.QSchedule.schedule;

@RequiredArgsConstructor
@Repository
public class MemberScheduleQueryRepositoryImpl implements MemberScheduleQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Long countOverlappedSchedule(Member user,
                                        Long scheduleId,
                                        LocalDate startDate,
                                        LocalTime startTime,
                                        LocalDate endDate,
                                        LocalTime endTime) {

        return query.select(memberSchedule.count())
                .from(memberSchedule)
                .leftJoin(memberSchedule.member, member)
                .leftJoin(memberSchedule.schedule, schedule)
                .where(member.eq(user).and(schedule.id.ne(scheduleId)).and(getOverlappingSchedule(startDate, startTime, endDate, endTime)))
                .fetchFirst();
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

//    private BooleanExpression isContainScheduleDate(LocalDate startDate, LocalDate endDate) {
//        return isPresent(startDate) && isPresent(endDate) ?
//                schedule.startDate.loe(endDate).and(schedule.endDate.goe(startDate)) : Expressions.FALSE;
//    }
//
//    private BooleanExpression eqStartDateAndEndDate(LocalDate startDate) {
//        return isPresent(startDate) ? schedule.endDate.eq(startDate) : Expressions.FALSE;
//    }
//
//    private BooleanExpression eqEndDateAndStartDate(LocalDate endDate) {
//        return isPresent(endDate) ? schedule.startDate.eq(endDate) : Expressions.FALSE;
//    }
//
//    private BooleanExpression eqStartTimeAndEndTime(LocalTime startTime) {
//        return isPresent(startTime) ? schedule.endTime.eq(startTime) : Expressions.FALSE;
//    }
//
//    private BooleanExpression eqEndTimeAndStartTime(LocalTime endTime) {
//        return isPresent(endTime) ? schedule.startTime.eq(endTime) : Expressions.FALSE;
//    }
//
//    private BooleanExpression checkContainExceptionOfStart(LocalDate startDate, LocalTime startTime) {
//        return eqStartDateAndEndDate(startDate).and(eqStartTimeAndEndTime(startTime));
//    }
//
//    private BooleanExpression checkContainExceptionOfEnd(LocalDate endDate, LocalTime endTime) {
//        return eqEndDateAndStartDate(endDate).and(eqEndTimeAndStartTime(endTime));
//    }
//
//    private BooleanExpression getOverlappingSchedule(LocalDate startDate,
//                                                     LocalTime startTime,
//                                                     LocalDate endDate,
//                                                     LocalTime endTime) {
//        BooleanExpression containScheduleDate = isContainScheduleDate(startDate, endDate);
//        if (containScheduleDate != null) {
//            BooleanExpression checkStartException = checkContainExceptionOfStart(startDate, startTime);
//            BooleanExpression checkEndException = checkContainExceptionOfEnd(endDate, endTime);
//            return containScheduleDate.and(checkEndException.or(checkStartException));
//        } else {
//            return Expressions.FALSE;
//        }
//    }


}
