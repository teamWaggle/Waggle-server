package com.example.waggle.domain.board.persistence.dao.question.querydsl;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.persistence.entity.QQuestion.question;
import static com.example.waggle.domain.hashtag.persistence.entity.QBoardHashtag.boardHashtag;
import static com.example.waggle.domain.hashtag.persistence.entity.QHashtag.hashtag;
import static com.example.waggle.domain.recommend.persistence.entity.QRecommend.recommend;


@Repository
@RequiredArgsConstructor
public class QuestionQueryRepositoryImpl implements QuestionQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Question> findQuestionsBySortParam(QuestionSortParam sortParam, Pageable pageable) {
        JPAQuery<Question> baseQuery = queryFactory.selectFrom(question);
        JPAQuery<Long> countQuery = queryFactory.select(question.count()).from(question);
        if (sortParam == QuestionSortParam.RECOMMEND) {
            baseQuery
                    .leftJoin(recommend).on(recommend.board.eq(question._super))
                    .groupBy(question);
            countQuery
                    .leftJoin(recommend).on(recommend.board.eq(question._super))
                    .groupBy(question);
        }
        List<Question> questionList = baseQuery
                .orderBy(createSortingOrder(sortParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(questionList, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<Question> findQuestionsByKeyword(String keyword, Pageable pageable) {
        List<Question> questionList = queryFactory
                .selectFrom(question)
                .join(question.boardHashtags, boardHashtag)
                .join(boardHashtag.hashtag, hashtag)
                .where(hashtag.content.containsIgnoreCase(keyword).or(question.title.containsIgnoreCase(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(question.count())
                .from(question)
                .join(question.boardHashtags, boardHashtag)
                .join(boardHashtag.hashtag, hashtag)
                .where(hashtag.content.containsIgnoreCase(keyword).or(question.title.containsIgnoreCase(keyword)));
        return PageableExecutionUtils.getPage(questionList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Question> findQuestionListByKeywordAndSortParam(String keyword, QuestionSortParam sortParam, Pageable pageable) {
        JPAQuery<Question> baseQuery = queryFactory.selectFrom(question);
        JPAQuery<Long> countQuery = queryFactory.select(question.count()).from(question);
        if (keyword != null || !keyword.isEmpty()) {
            baseQuery.join(question.boardHashtags, boardHashtag)
                    .join(boardHashtag.hashtag, hashtag)
                    .where(hashtag.content.containsIgnoreCase(keyword).or(question.title.containsIgnoreCase(keyword)));
            countQuery.join(question.boardHashtags, boardHashtag)
                    .join(boardHashtag.hashtag, hashtag)
                    .where(hashtag.content.containsIgnoreCase(keyword).or(question.title.containsIgnoreCase(keyword)));
        }
        if (sortParam.equals(QuestionSortParam.RECOMMEND)) {
            baseQuery
                    .leftJoin(recommend).on(recommend.board.eq(question._super))
                    .groupBy(question);
            countQuery
                    .leftJoin(recommend).on(recommend.board.eq(question._super))
                    .groupBy(question);
        }
        List<Question> questionList = baseQuery.orderBy(createSortingOrder(sortParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(questionList, pageable, countQuery::fetchCount);
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
