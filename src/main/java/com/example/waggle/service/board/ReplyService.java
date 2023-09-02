package com.example.waggle.service.board;


import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.comment.MemberMention;
import com.example.waggle.domain.board.comment.Reply;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.comment.CommentViewDto;
import com.example.waggle.dto.board.reply.ReplyViewDto;
import com.example.waggle.dto.board.reply.ReplyWriteDto;

import com.example.waggle.exception.CustomPageException;
import com.example.waggle.repository.board.comment.CommentRepository;
import com.example.waggle.repository.board.comment.ReplyRepository;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.service.board.util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.example.waggle.exception.ErrorCode.REPLY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReplyService {

    private final MemberRepository memberRepository;
    private final UtilService utilService;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;


    //1. 조회
    @Transactional(readOnly = true)
    public List<ReplyViewDto> findReplies (Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));
        return comment.getReplies().stream()
                .map(ReplyViewDto::toDto).collect(Collectors.toList());
    }
    //2. 저장
    public Long saveReply(CommentViewDto commentViewDto, ReplyWriteDto replyWriteDto) {
        Member member = utilService.getSignInMember();

        //check exist
        Comment comment = commentRepository.findById(commentViewDto.getId())
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));

        //SAVE reply and LINK member&comment
        Reply reply = replyWriteDto.toEntity(member);
        comment.addReply(reply);
        replyRepository.save(reply);

        //auto persist
        for (String mentionMember : replyWriteDto.getMentionMembers()) {
            if (memberRepository.existsByUsername(mentionMember)) {
                reply.addMemberMention(MemberMention.builder()
                        .username(mentionMember)
                        .build()
                );
            }
            log.info("not exist member username!");
        }

        return reply.getId();
    }
    //3. 수정
    public boolean checkMember(ReplyViewDto viewDto) {
        Member member = utilService.getSignInMember();
        Reply reply = replyRepository.findById(viewDto.getId())
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));
        return reply.getMember().equals(member);
    }
    public Long changeReply(ReplyViewDto replyViewDto, ReplyWriteDto replyWriteDto) {
        //find
        Reply reply = replyRepository.findById(replyViewDto.getId())
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));
        //edit
        reply.changeContent(replyWriteDto.getContent());
        reply.getMemberMentions().clear();
        for (String mentionMember : replyWriteDto.getMentionMembers()) {
            if (memberRepository.existsByUsername(mentionMember)) {
                reply.addMemberMention(MemberMention.builder()
                        .username(mentionMember)
                        .build()
                );
            }
            log.info("not exist member username!");
        }
        return reply.getId();
    }

    //4. 삭제
    public void deleteReply(ReplyViewDto viewDto) {
        Member member = utilService.getSignInMember();
        Reply reply = replyRepository.findById(viewDto.getId())
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));
        if (reply.getMember().equals(member)) {
            log.info("delete completely!");
            replyRepository.delete(reply);
        }
    }

}
