package com.example.waggle.web.converter;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.global.util.PageUtil;
import com.example.waggle.web.dto.comment.CommentResponse.CommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.CommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentViewDto;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;

public class CommentConverter {

    public static CommentViewDto toCommentViewDto(Comment comment) {
        return CommentViewDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .member(MemberConverter.toMemberSummaryDto(comment.getMember()))
                .build();
    }

    public static CommentListDto toCommentListDto(Page<Comment> pagedComment) {
        List<CommentViewDto> collect = pagedComment.stream()
                .map(CommentConverter::toCommentViewDto).collect(Collectors.toList());
        return CommentListDto.builder()
                .commentList(collect)
                .nextPageParam(PageUtil.countNextPage(pagedComment))
                .build();
    }

    public static QuestionCommentViewDto toQuestionCommentViewDto(Comment comment) {
        Board board = comment.getBoard();
        Question question = (Question) Hibernate.unproxy(board);
        return QuestionCommentViewDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .questionTitle(question.getTitle())
                .status(question.getStatus())
                .createdDate(comment.getCreatedDate())
                .member(MemberConverter.toMemberSummaryDto(comment.getMember()))
                .build();
    }

    public static QuestionCommentListDto toQuestionCommentListDto(Page<Comment> pagedComments) {
        List<QuestionCommentViewDto> commentList = pagedComments.stream()
                .map(CommentConverter::toQuestionCommentViewDto).collect(Collectors.toList());
        return QuestionCommentListDto.builder()
                .commentList(commentList)
                .nextPageParam(PageUtil.countNextPage(pagedComments))
                .build();
    }

    public static SirenCommentViewDto toSirenCommentViewDto(Comment comment) {
        Board board = comment.getBoard();
        Siren siren = (Siren) Hibernate.unproxy(board);
        return SirenCommentViewDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .sirenTitle(siren.getTitle())
                .status(siren.getStatus())
                .category(siren.getCategory())
                .createdDate(comment.getCreatedDate())
                .member(MemberConverter.toMemberSummaryDto(comment.getMember()))
                .build();
    }

    public static SirenCommentListDto toSirenCommentListDto(Page<Comment> pagedComments) {
        List<SirenCommentViewDto> commentList = pagedComments.stream()
                .map(CommentConverter::toSirenCommentViewDto).collect(Collectors.toList());
        return SirenCommentListDto.builder()
                .commentList(commentList)
                .nextPageParam(PageUtil.countNextPage(pagedComments))
                .build();
    }
}
