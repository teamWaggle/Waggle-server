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
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.persistence.entity.QStory.story;
import static com.example.waggle.domain.recommend.persistence.entity.QRecommend.recommend;


@Repository
@RequiredArgsConstructor
public class StoryQueryRepositoryImpl implements StoryQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Page<Story> findStoriesBySortParam(StorySortParam sortParam, Pageable pageable) {
        JPAQuery<Story> baseQuery = query.selectFrom(story);
        if (sortParam.equals(StorySortParam.RECOMMEND)) {
            baseQuery.leftJoin(recommend).on(story._super.eq(recommend.board));
            baseQuery.groupBy(story);
        }
        List<Story> storyList = baseQuery
                .orderBy(createSortingOrder(sortParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = query.select(story.count())
                .from(story)
                .fetchOne();
        return new PageImpl<>(storyList, pageable, count);
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
