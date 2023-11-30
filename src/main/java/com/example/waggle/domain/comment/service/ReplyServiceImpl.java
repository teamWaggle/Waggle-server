package com.example.waggle.domain.comment.service;


import static com.example.waggle.global.exception.ErrorCode.CANNOT_TOUCH_NOT_YOURS;
import static com.example.waggle.global.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.example.waggle.global.exception.ErrorCode.REPLY_NOT_FOUND;

import com.example.waggle.domain.comment.domain.Comment;
import com.example.waggle.domain.comment.domain.Reply;
import com.example.waggle.domain.comment.dto.ReplyViewDto;
import com.example.waggle.domain.comment.dto.ReplyWriteDto;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.domain.mention.domain.Mention;
import com.example.waggle.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        Reply reply = replyWriteDto.toEntity(member, comment);
        comment.addReply(reply);
        replyRepository.save(reply);

        addMentionsToReply(reply, replyWriteDto.getMentions());

        return reply.getId();
    }

    @Transactional
    @Override
    public Long updateReply(Long replyId, ReplyWriteDto replyWriteDto) {
        Reply reply = getReplyById(replyId);
        reply.changeContent(replyWriteDto.getContent());
        reply.getMentions().clear();
        addMentionsToReply(reply, replyWriteDto.getMentions());
        return reply.getId();
    }

    @Transactional
    @Override
    public void deleteReply(Long replyId) {
        Reply reply = getReplyById(replyId);
        if (!validateMember(replyId)) {
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }
        replyRepository.delete(reply);
    }

    @Override
    public boolean validateMember(Long replyId) {
        Member member = utilService.getSignInMember();
        Reply reply = getReplyById(replyId);
        return reply.getMember().equals(member);
    }

    private Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));
    }

    private void addMentionsToReply(Reply reply, List<String> mentions) {
        for (String mention : mentions) {
            if (memberRepository.existsByUsername(mention)) {
                reply.addMention(Mention.builder()
                        .username(mention)
                        .build()
                );
            }
        }
    }

}
