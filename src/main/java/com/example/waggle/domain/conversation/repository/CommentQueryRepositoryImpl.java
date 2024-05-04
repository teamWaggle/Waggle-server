package com.example.waggle.domain.conversation.repository;

import static com.example.waggle.domain.board.question.entity.QQuestion.question;
import static com.example.waggle.domain.board.siren.entity.QSiren.siren;
import static com.example.waggle.domain.conversation.entity.QComment.comment;

import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentViewDto;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public Page<SirenCommentViewDto> findPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable) {
        List<SirenCommentViewDto> content = queryFactory
                .select(Projections.constructor(SirenCommentViewDto.class,
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
                                comment.member.profileImgUrl.as("profileImgUrl")
                        )
                ))
                .from(siren)
                .join(siren.comments, comment)
                .where(siren.member.userUrl.eq(userUrl))
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
        List<QuestionCommentViewDto> content = queryFactory
                .select(Projections.constructor(QuestionCommentViewDto.class,
                        comment.id,
                        comment.content,
                        question.title,
                        question.status,
                        comment.createdDate,
                        Projections.fields(MemberSummaryDto.class,
                                comment.member.id.as("memberId"),
                                comment.member.userUrl.as("userUrl"),
                                comment.member.nickname.as("nickname"),
                                comment.member.profileImgUrl.as("profileImgUrl")
                        )
                ))
                .from(question)
                .join(question.comments, comment)
                .where(question.member.userUrl.eq(userUrl))
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
