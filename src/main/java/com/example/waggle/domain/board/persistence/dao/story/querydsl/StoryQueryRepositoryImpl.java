package com.example.waggle.domain.board.persistence.dao.story.querydsl;

import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StorySortParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.persistence.entity.QStory.story;
import static com.example.waggle.domain.hashtag.persistence.entity.QBoardHashtag.boardHashtag;
import static com.example.waggle.domain.hashtag.persistence.entity.QHashtag.hashtag;
import static com.example.waggle.domain.recommend.persistence.entity.QRecommend.recommend;


@Repository
@RequiredArgsConstructor
public class StoryQueryRepositoryImpl implements StoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Story> findStoriesBySortParam(StorySortParam sortParam, Pageable pageable) {
        JPAQuery<Story> baseQuery = queryFactory.selectFrom(story);
        if (sortParam.equals(StorySortParam.RECOMMEND)) {
            baseQuery.leftJoin(recommend).on(story._super.eq(recommend.board));
            baseQuery.groupBy(story);
        }
        List<Story> storyList = baseQuery
                .orderBy(createSortingOrder(sortParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(story.count())
                .from(story)
                .fetchOne();
        return new PageImpl<>(storyList, pageable, count);
    }

    @Override
    public Page<Story> findStoriesByKeyword(String keyword, Pageable pageable) {
        List<Story> storyList = queryFactory
                .selectFrom(story)
                .join(story.boardHashtags, boardHashtag)
                .join(boardHashtag.hashtag, hashtag)
                .where(hashtag.content.contains(keyword).or(story.content.contains(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(story.count())
                .from(story)
                .join(story.boardHashtags, boardHashtag)
                .join(boardHashtag.hashtag, hashtag)
                .where(hashtag.content.contains(keyword).or(story.content.contains(keyword)));
        return PageableExecutionUtils.getPage(storyList, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier[] createSortingOrder(StorySortParam sortParam) {
        switch (sortParam) {
            case LATEST -> {
                return new OrderSpecifier[]{story.createdDate.desc()};
            }
            case RECOMMEND -> {
                return new OrderSpecifier[]{
                        recommend.count().desc(),
                        story.createdDate.desc()
                };
            }
            default -> {
                return new OrderSpecifier[]{story.createdDate.desc()};
            }
        }
    }
}
