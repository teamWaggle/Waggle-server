package com.example.waggle.web.converter;

import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.comment.CommentResponse;
import com.example.waggle.web.dto.member.MemberResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static CommentResponse.ViewDto toViewDto(Comment comment) {
        Member member = comment.getMember();
        return CommentResponse.ViewDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .member(MemberResponse.SummaryDto.builder()
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .profileImgUrl(MediaUtil.getProfileImg(member))
                        .build())
                .build();
    }

    public static CommentResponse.ListDto toListDto(Page<Comment> pagedComment) {
        List<CommentResponse.ViewDto> collect = pagedComment.stream()
                .map(CommentConverter::toViewDto).collect(Collectors.toList());
        return CommentResponse.ListDto.builder()
                .commentList(collect)
                .isFirst(pagedComment.isFirst())
                .isLast(pagedComment.isLast())
                .totalComments(pagedComment.getTotalElements())
                .build();
    }
}
