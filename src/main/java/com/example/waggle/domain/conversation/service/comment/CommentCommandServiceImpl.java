package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.BoardRepository;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.repository.CommentRepository;
import com.example.waggle.domain.conversation.repository.ReplyRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.mention.service.MentionCommandService;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.handler.CommentHandler;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.comment.CommentRequest;
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
    private final MemberQueryService memberQueryService;
    private final MemberScheduleRepository memberScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final MentionCommandService mentionCommandService;
    private final BoardRepository boardRepository;

    @Override
    public Long createComment(Long boardId, CommentRequest.Post commentWriteDto) {
        Member signInMember = memberQueryService.getSignInMember();
        validateAccessSchedule(boardId, signInMember);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
        Comment build = Comment.builder()
                .content(commentWriteDto.getContent())
                .board(board)
                .member(signInMember)
                .build();
        commentRepository.save(build);
        mentionCommandService.createMentions(build, commentWriteDto.getMentionedNickname());
        return build.getId();
    }

    @Override
    public Long createCommentByUsername(Long boardId, CommentRequest.Post commentWriteDto, String username) {
        Member memberByUsername = memberQueryService.getMemberByUsername(username);
        validateAccessSchedule(boardId, memberByUsername);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
        Comment build = Comment.builder()
                .content(commentWriteDto.getContent())
                .board(board)
                .member(memberByUsername)
                .build();
        commentRepository.save(build);
        mentionCommandService.createMentions(build, commentWriteDto.getMentionedNickname());
        return build.getId();
    }

    @Override
    public Long updateComment(Long commentId, CommentRequest.Post commentWriteDto) {
        Member signInMember = memberQueryService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        validateMember(comment, signInMember);

        comment.changeContent(commentWriteDto.getContent());
        mentionCommandService.updateMentions(comment, commentWriteDto.getMentionedNickname());
        return comment.getId();
    }

    @Override
    public Long updateCommentByUsername(Long commentId, String username, CommentRequest.Post commentWriteDto) {
        Member signInMember = memberQueryService.getMemberByUsername(username);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        validateMember(comment, signInMember);

        comment.changeContent(commentWriteDto.getContent());
        mentionCommandService.updateMentions(comment, commentWriteDto.getMentionedNickname());
        return comment.getId();
    }

    @Override
    public void deleteComment(Long commentId) {
        Member signInMember = memberQueryService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        validateMember(comment, signInMember);

        replyRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentByUsername(Long commentId, String username) {
        Member signInMember = memberQueryService.getMemberByUsername(username);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        validateMember(comment, signInMember);

        replyRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentForHardReset(Long commentId) {
        commentRepository.findById(commentId).ifPresent(
                comment -> {
                    replyRepository.deleteAllByCommentId(commentId);
                    commentRepository.delete(comment);
                }
        );
    }

    public void validateMember(Comment comment, Member member) {
        if (!comment.getMember().equals(member)) {
            throw new CommentHandler(ErrorStatus.COMMENT_CANNOT_EDIT_OTHERS);
        }
    }

    private void validateAccessSchedule(Long boardId, Member signInMember) {
        if (scheduleRepository.existsById(boardId) && memberScheduleRepository.existsByMemberIdAndScheduleId(signInMember.getId(), boardId)) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_CANNOT_COMMENTED_BECAUSE_OF_ACCESS);
        }
    }

}
