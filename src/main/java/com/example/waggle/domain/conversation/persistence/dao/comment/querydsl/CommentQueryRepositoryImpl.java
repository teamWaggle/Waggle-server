package com.example.waggle.domain.conversation.persistence.dao.comment.querydsl;


import com.example.waggle.domain.conversation.presentation.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.domain.conversation.presentation.dto.comment.CommentResponse.SirenCommentViewDto;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.waggle.domain.board.persistence.entity.QQuestion.question;
import static com.example.waggle.domain.board.persistence.entity.QSiren.siren;
import static com.example.waggle.domain.conversation.persistence.entity.QComment.comment;

@Repository
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public Page<SirenCommentViewDto> findPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable) {
        List<SirenCommentViewDto> content = queryFactory.select(
                        Projections.constructor(SirenCommentViewDto.class,
                                comment.id,
                                comment.content,
                                siren.title,
                                siren.status,
                                siren.category,
                                comment.createdDate,
                                Projections.fields(MemberSummaryDto.class,
                                        comment.member.id.as("memberId"),
                                        comment.member.userUrl.as("userUrl"),
                                        comment.member.nickname.as("nickname"),
                                        comment.member.profileImgUrl.as("profileImgUrl"))))
                .from(siren)
                .join(siren.comments, comment)
                .where(comment.member.userUrl.eq(userUrl))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdDate.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(siren)
                .join(siren.comments, comment)
                .where(siren.member.userUrl.eq(userUrl))
                .stream().count();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<QuestionCommentViewDto> findPagedQuestionCommentsByUserUrl(String userUrl, Pageable pageable) {
        List<QuestionCommentViewDto> content = queryFactory.select(
                        Projections.constructor(QuestionCommentViewDto.class,
                                comment.id,
                                comment.content,
                                question.title,
                                question.status,
                                comment.createdDate,
                                Projections.fields(MemberSummaryDto.class,
                                        comment.member.id.as("memberId"),
                                        comment.member.userUrl.as("userUrl"),
                                        comment.member.nickname.as("nickname"),
                                        comment.member.profileImgUrl.as("profileImgUrl"))))
                .from(question)
                .join(question.comments, comment)
                .where(comment.member.userUrl.eq(userUrl))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdDate.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(question)
                .join(question.comments, comment)
                .where(question.member.userUrl.eq(userUrl))
                .stream().count();

        return new PageImpl<>(content, pageable, total);
    }
}
