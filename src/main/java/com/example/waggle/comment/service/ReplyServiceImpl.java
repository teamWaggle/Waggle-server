package com.example.waggle.comment.service;


import com.example.waggle.comment.domain.Comment;
import com.example.waggle.comment.domain.Reply;
import com.example.waggle.comment.dto.CommentViewDto;
import com.example.waggle.comment.dto.ReplyViewDto;
import com.example.waggle.comment.dto.ReplyWriteDto;
import com.example.waggle.comment.repository.CommentRepository;
import com.example.waggle.comment.repository.ReplyRepository;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.memberMention.domain.MemberMention;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.*;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReplyServiceImpl implements ReplyService {

    private final MemberRepository memberRepository;
    private final UtilService utilService;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Override
    public List<ReplyViewDto> getReplies(Long commentId) {
        List<Reply> replies = replyRepository.findByCommentId(commentId);
        return replies.stream().map(ReplyViewDto::toDto).collect(Collectors.toList());
    }
    @Transactional
    @Override
    public Long createReply(Long commentId, ReplyWriteDto replyWriteDto) {
        Member member = utilService.getSignInMember();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));

        Reply reply = replyWriteDto.toEntity(member);
        comment.addReply(reply);
        replyRepository.save(reply);

        for (String mentionMember : replyWriteDto.getMentionMembers()) {
            if (memberRepository.existsByUsername(mentionMember)) {
                reply.addMemberMention(MemberMention.builder()
                        .username(mentionMember)
                        .build()
                );
            }
        }

        return reply.getId();
    }

    @Transactional
    @Override
    public Long updateReply(Long replyId, ReplyWriteDto replyWriteDto) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));

        reply.changeContent(replyWriteDto.getContent());
        reply.getMemberMentions().clear();
        for (String mentionMember : replyWriteDto.getMentionMembers()) {
            if (memberRepository.existsByUsername(mentionMember)) {
                reply.addMemberMention(MemberMention.builder()
                        .username(mentionMember)
                        .build()
                );
            }
        }
        return reply.getId();
    }

    @Transactional
    @Override
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));
        if (!validateMember(replyId)) {
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }
        replyRepository.delete(reply);
    }

    @Override
    public boolean validateMember(Long replyId) {
        Member member = utilService.getSignInMember();
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));
        return reply.getMember().equals(member);
    }

}
