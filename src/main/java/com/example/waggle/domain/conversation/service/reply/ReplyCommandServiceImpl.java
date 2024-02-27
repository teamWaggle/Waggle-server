package com.example.waggle.domain.conversation.service.reply;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.conversation.repository.CommentRepository;
import com.example.waggle.domain.conversation.repository.ReplyRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.mention.service.MentionCommandService;
import com.example.waggle.global.exception.handler.CommentHandler;
import com.example.waggle.global.exception.handler.ReplyHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.reply.ReplyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyCommandServiceImpl implements ReplyCommandService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberQueryService memberQueryService;
    private final MentionCommandService mentionCommandService;

    @Override
    public Long createReply(Long commentId, ReplyRequest createReplyRequest) {
        Member member = memberQueryService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply reply = Reply.builder().member(member).comment(comment).content(createReplyRequest.getContent()).build();
        replyRepository.save(reply);

        mentionCommandService.createMentions(reply, createReplyRequest.getMentionedMemberList());
        return reply.getId();
    }

    @Override
    public Long createReplyByUsername(Long commentId, ReplyRequest createReplyRequest, String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply reply = Reply.builder().member(member).comment(comment).content(createReplyRequest.getContent()).build();
        replyRepository.save(reply);

        mentionCommandService.createMentions(reply, createReplyRequest.getMentionedMemberList());
        return reply.getId();
    }

    @Override
    public Long updateReply(Long replyId, ReplyRequest updateReplyRequest) {
        if (!validateMember(replyId, SecurityUtil.getCurrentUsername())) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
        Reply reply = getReplyById(replyId);
        reply.changeContent(updateReplyRequest.getContent());

        mentionCommandService.updateMentions(reply, updateReplyRequest.getMentionedMemberList());
        return reply.getId();
    }

    @Override
    public Long updateReplyByUsername(Long replyId, String username, ReplyRequest updateReplyRequest) {
        if (!validateMember(replyId, username)) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
        Reply reply = getReplyById(replyId);
        reply.changeContent(updateReplyRequest.getContent());

        mentionCommandService.updateMentions(reply, updateReplyRequest.getMentionedMemberList());
        return reply.getId();
    }

    @Override
    public void deleteReply(Long replyId) {
        if (!validateMember(replyId, SecurityUtil.getCurrentUsername())) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
        Reply reply = getReplyById(replyId);
        replyRepository.delete(reply);
    }

    @Override
    public void deleteReply(Long replyId, String username) {
        if (!validateMember(replyId, username)) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
        Reply reply = getReplyById(replyId);
        replyRepository.delete(reply);
    }

    private Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyHandler(ErrorStatus.REPLY_NOT_FOUND));
    }

    public boolean validateMember(Long replyId, String username) {
        Reply reply = getReplyById(replyId);
        return reply.getMember().getUsername()
                .equals(username);
    }
}
