package com.example.waggle.domain.board.persistence.dao.siren.querydsl;

import com.example.waggle.domain.board.persistence.entity.ResolutionStatus;
import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import com.example.waggle.domain.board.presentation.dto.siren.SirenSortParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.persistence.entity.QSiren.siren;
import static com.example.waggle.domain.recommend.persistence.entity.QRecommend.recommend;

@Repository
@RequiredArgsConstructor
public class SirenQueryRepositoryImpl implements SirenQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Siren> findSirensByFilterAndSort(SirenFilterParam filterParam, SirenSortParam sortParam, Pageable pageable) {
        JPAQuery<Siren> baseQuery = queryFactory.selectFrom(siren);
        if (sortParam.equals(SirenSortParam.RECOMMEND)) {
            baseQuery.leftJoin(recommend).on(siren._super.eq(recommend.board));
            baseQuery.groupBy(siren);
        }
        List<Siren> sirenList = baseQuery
                .orderBy(createSortingOrder(sortParam))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(selectCategory(filterParam))
                .fetch();
        Long count = queryFactory.select(siren.count())
                .from(siren)
                .fetchOne();
        return new PageImpl<>(sirenList, pageable, count);
    }

    @Override
    public List<Siren> findRandomUnresolvedSirens() {
        return queryFactory.selectFrom(siren)
                .where(siren.status.eq(ResolutionStatus.UNRESOLVED))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(4)
                .fetch();
    }

    @Override
    public Page<Siren> findSirensByKeyword(String keyword, Pageable pageable) {
        List<Siren> sirenList = queryFactory
                .selectFrom(siren)
                .where(siren.title.contains(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory
                .select(siren.count())
                .from(siren)
                .fetchOne();
        return new PageImpl<>(sirenList, pageable, count);
    }

    private OrderSpecifier[] createSortingOrder(SirenSortParam sortParam) {
        switch (sortParam) {
            case RECOMMEND -> {
                return new OrderSpecifier[]{
                        recommend.count().desc(),
                        siren.createdDate.desc()
                };
            }
            case RESOLVED -> {
                return new OrderSpecifier[]{
                        siren.status.asc(),
                        siren.createdDate.desc()
                };
            }
            case UNRESOLVED -> {
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

    private BooleanExpression selectCategory(SirenFilterParam filterParam) {
        return filterParam == SirenFilterParam.ALL ? null : siren.category.eq(filterParam.getCategory());
    }
}
