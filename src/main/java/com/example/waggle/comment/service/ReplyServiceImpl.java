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

import static com.example.waggle.commons.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.REPLY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    private final MemberRepository memberRepository;
    private final UtilService utilService;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;


    //1. 조회
    @Transactional(readOnly = true)
    @Override
    public List<ReplyViewDto> findReplies(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));
        return comment.getReplies().stream()
                .map(ReplyViewDto::toDto).collect(Collectors.toList());
    }

    //2. 저장
    @Override
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
    @Override
    public boolean checkMember(ReplyViewDto viewDto) {
        Member member = utilService.getSignInMember();
        Reply reply = replyRepository.findById(viewDto.getId())
                .orElseThrow(() -> new CustomPageException(REPLY_NOT_FOUND));
        return reply.getMember().equals(member);
    }

    @Override
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
    @Override
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
