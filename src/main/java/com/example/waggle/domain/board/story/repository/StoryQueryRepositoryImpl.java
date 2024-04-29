package com.example.waggle.domain.board.story.repository;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.web.dto.story.StoryFilterParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.story.entity.QStory.story;
import static com.example.waggle.domain.recommend.entity.QRecommend.recommend;

@Repository
@RequiredArgsConstructor
public class StoryQueryRepositoryImpl implements StoryQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Page<Story> findStoriesByFilter(StoryFilterParam filterParam, Pageable pageable) {
        JPAQuery<Story> baseQuery = query.selectFrom(story);
        if (filterParam.equals(StoryFilterParam.recommend)) {
            baseQuery.leftJoin(recommend).on(story._super.eq(recommend.board));
            baseQuery.groupBy(story);
        }
        List<Story> storyList = baseQuery
                .orderBy(createOrderFilter(filterParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = query.select(story.count())
                .from(story)
                .fetchOne();
        return new PageImpl<>(storyList, pageable, count);
    }

    private OrderSpecifier[] createOrderFilter(StoryFilterParam filterParam) {
        switch (filterParam) {
            case latest -> {
                return new OrderSpecifier[]{story.createdDate.desc()};
            }
            case recommend -> {
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
