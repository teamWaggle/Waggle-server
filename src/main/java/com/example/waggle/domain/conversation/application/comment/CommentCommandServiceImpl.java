package com.example.waggle.domain.conversation.application.comment;

import static com.example.waggle.domain.notification.presentation.dto.NotificationRequest.CommentDto;

import com.example.waggle.domain.board.persistence.dao.board.jpa.BoardRepository;
import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.conversation.persistence.dao.reply.ReplyRepository;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.conversation.presentation.dto.ConversationRequest;
import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.notification.persistence.dao.NotificationRepository;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.persistence.entity.NotificationType;
import com.example.waggle.domain.notification.presentation.dto.NotificationRequest.MentionDto;
import com.example.waggle.domain.schedule.persistence.dao.jpa.MemberScheduleRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.ScheduleRepository;
import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.object.handler.CommentHandler;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.object.handler.ScheduleHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.ObjectUtil;
import com.example.waggle.global.util.ParseUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberScheduleRepository memberScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final BoardRepository boardRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createComment(Long boardId, ConversationRequest createCommentRequest, Member member) {
        validateAccessSchedule(boardId, member);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));

        Comment comment = buildComment(board, createCommentRequest, member);
        commentRepository.save(comment);

        //MENTION
        List<Notification> notificationList = buildMentionNotification(comment, buildMentionDto(comment));

        //COMMENT
        notificationList.add(buildCommentNotification(comment, buildCommentDto(comment)));
        notificationRepository.saveAll(notificationList);
        return comment.getId();
    }

    @Override
    public Long updateComment(Long commentId, ConversationRequest updateCommentRequest, Member member) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        validateMember(comment, member);

        comment.changeContent(updateCommentRequest.getContent());
        //MENTION
        List<Notification> notificationList = buildMentionNotification(comment, buildMentionDto(comment));
        notificationRepository.saveAll(notificationList);
        return comment.getId();
    }

    @Override
    public void deleteComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        validateMember(comment, member);

        replyRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId, Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION);
        }
        commentRepository.deleteCommentsWithRelations(commentId);
    }

    public void validateMember(Comment comment, Member member) {
        if (!comment.getMember().equals(member)) {
            throw new CommentHandler(ErrorStatus.COMMENT_CANNOT_EDIT_OTHERS);
        }
    }

    private void validateAccessSchedule(Long boardId, Member signInMember) {
        if (scheduleRepository.existsById(boardId) && !memberScheduleRepository.existsByMemberIdAndScheduleId(
                signInMember.getId(), boardId)) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_CANNOT_COMMENTED_BECAUSE_OF_ACCESS);
        }
    }

    private Comment buildComment(Board board, ConversationRequest createCommentRequest, Member member) {
        return Comment.builder()
                .content(createCommentRequest.getContent())
                .board(board)
                .member(member)
                .build();
    }

    private Notification buildCommentNotification(Comment comment, CommentDto content) {
        return Notification.builder()
                .sender(comment.getMember())
                .receiver(comment.getBoard().getMember())
                .isRead(false)
                .type(NotificationType.COMMENT)
                .content(ObjectUtil.serialize(content))
                .build();
    }

    private List<Notification> buildMentionNotification(Comment comment, MentionDto content) {
        return ParseUtil.parsingUserUrl(comment)
                .stream()
                .map(userUrl -> memberRepository.findByUserUrl(userUrl)
                        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND)))
                .map(receiver -> Notification.builder()
                        .sender(comment.getMember())
                        .receiver(receiver)
                        .type(NotificationType.MENTIONED)
                        .isRead(false)
                        .content(ObjectUtil.serialize(content))
                        .build())
                .collect(Collectors.toList());
    }

    private CommentDto buildCommentDto(Comment comment) {
        return CommentDto.builder()
                .commentContent(comment.getContent())
                .boardType(BoardType.fromDiscriminatorValue(comment.getBoard()))
                .build();
    }

    private MentionDto buildMentionDto(Comment comment) {
        return MentionDto.builder()
                .conversationContent(comment.getContent())
                .build();
    }
}
