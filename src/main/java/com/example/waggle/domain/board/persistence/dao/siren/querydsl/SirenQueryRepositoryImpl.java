package com.example.waggle.domain.board.persistence.dao.siren.querydsl;

import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.siren.entity.QSiren.siren;
import static com.example.waggle.domain.recommend.entity.QRecommend.recommend;

@Repository
@RequiredArgsConstructor
public class SirenQueryRepositoryImpl implements SirenQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Page<Siren> findSirensByFilter(SirenFilterParam filterParam, Pageable pageable) {
        JPAQuery<Siren> baseQuery = query.selectFrom(siren);
        if (filterParam.equals(SirenFilterParam.recommend)) {
            baseQuery.leftJoin(recommend).on(siren._super.eq(recommend.board));
            baseQuery.groupBy(siren);
        }
        List<Siren> sirenList = baseQuery
                .orderBy(createOrderFilter(filterParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = query.select(siren.count())
                .from(siren)
                .fetchOne();
        return new PageImpl<>(sirenList, pageable, count);
    }

    private OrderSpecifier[] createOrderFilter(SirenFilterParam filterParam) {
        switch (filterParam) {
            case latest -> {
                return new OrderSpecifier[]{siren.createdDate.desc()};
            }
            case recommend -> {
                return new OrderSpecifier[]{
                        recommend.count().desc(),
                        siren.createdDate.desc()
                };
            }
            case resolved -> {
                return new OrderSpecifier[]{
                        siren.status.asc(),
                        siren.createdDate.desc()
                };
            }
            case unresolved -> {
                return new OrderSpecifier[]{
                        siren.status.desc(),
                        siren.createdDate.desc()
                };
            }
            default -> {
                return new OrderSpecifier[]{siren.createdDate.desc()};
            }
        }
    }
}
