package com.example.waggle.domain.board.persistence.dao.question.querydsl;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.persistence.entity.QQuestion.question;
import static com.example.waggle.domain.recommend.persistence.entity.QRecommend.recommend;


@Repository
@RequiredArgsConstructor
public class QuestionQueryRepositoryImpl implements QuestionQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Page<Question> findQuestionsBySortParam(QuestionSortParam sortParam, Pageable pageable) {
        JPAQuery<Question> baseQuery = query.selectFrom(question);
        if (sortParam == QuestionSortParam.RECOMMEND) {
            baseQuery.leftJoin(recommend).on(recommend.board.eq(question._super));
            baseQuery.groupBy(question);
        }
        List<Question> questionList = baseQuery
                .orderBy(createSortingOrder(sortParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(question.count())
                .from(question)
                .fetchOne();

        return new PageImpl<>(questionList, pageable, count);
    }

    private OrderSpecifier[] createSortingOrder(QuestionSortParam sortParam) {
        switch (sortParam) {
            case LATEST -> {
                return new OrderSpecifier[]{question.createdDate.desc()};
            }
            case RECOMMEND -> {
                return new OrderSpecifier[]{
                        recommend.count().desc(),
                        question.createdDate.desc()
                };
            }
            case RESOLVED -> {
                return new OrderSpecifier[]{
                        question.status.asc(),
                        question.createdDate.desc()
                };
            }
            case UNRESOLVED -> {
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
