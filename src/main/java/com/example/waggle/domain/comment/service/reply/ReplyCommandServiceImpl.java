package com.example.waggle.domain.comment.service.reply;

import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.mention.entity.Mention;
import com.example.waggle.domain.mention.repository.MentionRepository;
import com.example.waggle.global.exception.handler.CommentHandler;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.ReplyHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.reply.ReplyRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyCommandServiceImpl implements ReplyCommandService {

    private final MemberRepository memberRepository;
    private final UtilService utilService;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MentionRepository mentionRepository;

    @Override
    public Long createReply(Long commentId, ReplyRequest.Post replyWriteDto) {
        Member member = utilService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply reply = Reply.builder().member(member).comment(comment).content(replyWriteDto.getContent()).build();
        replyRepository.save(reply);

        addMentionsToReply(reply, replyWriteDto.getMentions());
        return reply.getId();
    }

    @Override
    public Long updateReply(Long replyId, ReplyRequest.Post replyWriteDto) {
        if (!validateMember(replyId)) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
        Reply reply = getReplyById(replyId);
        reply.changeContent(replyWriteDto.getContent());
        reply.getMentions().clear();
        addMentionsToReply(reply, replyWriteDto.getMentions());
        return reply.getId();
    }

    @Override
    public void deleteReply(Long replyId) {
        if (!validateMember(replyId)) {
            throw new ReplyHandler(ErrorStatus.REPLY_CANNOT_EDIT_OTHERS);
        }
        Reply reply = getReplyById(replyId);
        replyRepository.delete(reply);
    }

    private Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyHandler(ErrorStatus.REPLY_NOT_FOUND));
    }

    private void addMentionsToReply(Reply reply, List<String> mentions) {
        mentions.stream().forEach(m -> {
            Member member = memberRepository.findByUsername(m)
                    .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));        //TODO 하나라도 not found -> error?
            Mention build = Mention.builder().reply(reply).member(member).build();
            reply.addMention(build);    //save -> cascade.persist
        }
        );
    }

    public boolean validateMember(Long replyId) {
        Member member = utilService.getSignInMember();
        Reply reply = getReplyById(replyId);
        return reply.getMember().equals(member);
    }
}
