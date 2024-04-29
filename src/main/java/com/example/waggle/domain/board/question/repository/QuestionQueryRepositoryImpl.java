package com.example.waggle.domain.board.question.repository;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.web.dto.question.QuestionFilterParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.question.entity.QQuestion.question;
import static com.example.waggle.domain.recommend.entity.QRecommend.recommend;

@Repository
@RequiredArgsConstructor
public class QuestionQueryRepositoryImpl implements QuestionQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Page<Question> findQuestionsByFilter(QuestionFilterParam filterParam, Pageable pageable) {
        JPAQuery<Question> baseQuery = query.selectFrom(question);
        if (filterParam == QuestionFilterParam.recommend) {
            baseQuery.leftJoin(recommend).on(recommend.board.eq(question._super));
            baseQuery.groupBy(question);
        }
        List<Question> questionList = baseQuery
                .orderBy(createOrderFilter(filterParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(question.count())
                .from(question)
                .fetchOne();

        return new PageImpl<>(questionList, pageable, count);
    }

    private OrderSpecifier[] createOrderFilter(QuestionFilterParam filterParam) {
        switch (filterParam) {
            case latest -> {
                return new OrderSpecifier[]{question.createdDate.desc()};
            }
            case recommend -> {
                return new OrderSpecifier[]{
                        recommend.count().desc(),
                        question.createdDate.desc()
                };
            }
            case resolved -> {
                return new OrderSpecifier[]{
                        question.status.asc(),
                        question.createdDate.desc()
                };
            }
            case unresolved -> {
                return new OrderSpecifier[]{
                        question.status.desc(),
                        question.createdDate.desc()
                };
            }
            default -> {
                return new OrderSpecifier[]{question.createdDate.desc()};
            }
        }
    }
}
