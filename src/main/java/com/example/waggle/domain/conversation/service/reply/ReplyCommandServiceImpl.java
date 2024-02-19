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
    public Long createReply(Long commentId, ReplyRequest.Post replyWriteDto) {
        Member member = memberQueryService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply reply = buildReply(member, replyWriteDto, comment);
        replyRepository.save(reply);

        mentionCommandService.createMentions(reply, replyWriteDto.getMentionedNickname());
        return reply.getId();
    }

    @Override
    public Long createReply(Long commentId, Member member, ReplyRequest.Post replyWriteDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply reply = buildReply(member, replyWriteDto, comment);
        replyRepository.save(reply);

        mentionCommandService.createMentions(reply, replyWriteDto.getMentionedNickname());
        return reply.getId();
    }

    @Override
    public Long updateReply(Long replyId, ReplyRequest.Post replyWriteDto) {
        Reply reply = getReplyById(replyId);
        Member member = memberQueryService.getSignInMember();
        validateMember(reply, member);
        reply.changeContent(replyWriteDto.getContent());

        mentionCommandService.updateMentions(reply, replyWriteDto.getMentionedNickname());
        return reply.getId();
    }

    @Override
    public Long updateReply(Long replyId, Member member, ReplyRequest.Post replyWriteDto) {
        Reply reply = getReplyById(replyId);
        validateMember(reply, member)
        reply.changeContent(replyWriteDto.getContent());

        mentionCommandService.updateMentions(reply, replyWriteDto.getMentionedNickname());
        return reply.getId();
    }

    @Override
    public void deleteReply(Long replyId) {
        Reply reply = getReplyById(replyId);
        Member member = memberQueryService.getSignInMember();
        validateMember(reply, member);
        replyRepository.delete(reply);
    }

    @Override
    public void deleteReply(Long replyId, Member member) {
        Reply reply = getReplyById(replyId);
        validateMember(reply, member);
        replyRepository.delete(reply);
    }

    private Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyHandler(ErrorStatus.REPLY_NOT_FOUND));
    }

    public boolean validateMember(Reply reply, Member member) {
        if (!reply.getMember().equals(member)) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
    }

    private static Reply buildReply(Member member, ReplyRequest.Post replyWriteDto, Comment comment) {
        return Reply.builder()
                .member(member)
                .comment(comment)
                .content(replyWriteDto.getContent())
                .build();
    }
}
