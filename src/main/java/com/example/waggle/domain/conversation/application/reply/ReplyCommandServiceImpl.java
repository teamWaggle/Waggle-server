package com.example.waggle.domain.conversation.application.reply;

import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.conversation.persistence.dao.reply.ReplyRepository;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.conversation.persistence.entity.Reply;
import com.example.waggle.domain.conversation.presentation.dto.reply.ReplyRequest;
import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.notification.persistence.dao.NotificationRepository;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.persistence.entity.NotificationType;
import com.example.waggle.exception.object.handler.CommentHandler;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.object.handler.ReplyHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyCommandServiceImpl implements ReplyCommandService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public Long createReply(Long commentId, ReplyRequest createReplyRequest, Member member) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply reply = buildReply(comment, createReplyRequest, member);
        replyRepository.save(reply);

        //MENTION
        List<Notification> notificationList = ParseUtil.parsingUserUrl(reply)
                .stream()
                .map(userUrl -> memberRepository.findByUserUrl(userUrl)
                        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND)))
                .map(receiver -> Notification.of(
                        member,
                        receiver,
                        NotificationType.MENTIONED,
                        reply.getContent()))
                .collect(Collectors.toList());
        notificationRepository.saveAll(notificationList);
        return reply.getId();
    }

    @Override
    public Long updateReply(Long replyId, ReplyRequest updateReplyRequest, Member member) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyHandler(ErrorStatus.REPLY_NOT_FOUND));
        validateMember(reply, member);
        reply.changeContent(updateReplyRequest.getContent());
        return reply.getId();
    }

    @Override
    public void deleteReply(Long replyId, Member member) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyHandler(ErrorStatus.REPLY_NOT_FOUND));
        validateMember(reply, member);
        replyRepository.delete(reply);
    }

    @Override
    public void deleteReplyByAdmin(Long replyId, Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION);
        }
        replyRepository.deleteById(replyId);
    }

    public void validateMember(Reply reply, Member member) {
        if (!reply.getMember().equals(member)) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
    }

    private static Reply buildReply(Comment comment, ReplyRequest createReplyRequest, Member member) {
        return Reply.builder()
                .member(member)
                .comment(comment)
                .content(createReplyRequest.getContent())
                .build();
    }
}
