package com.example.waggle.domain.comment.service.reply;

import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.mention.entity.Mention;
import com.example.waggle.global.exception.handler.CommentHandler;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.ReplyHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.reply.ReplyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyCommandServiceImpl implements ReplyCommandService {

    private final MemberRepository memberRepository;
    private final UtilService utilService;
    private final ReplyQueryService replyQueryService;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Override
    public Long createReply(Long commentId, ReplyRequest.Post replyWriteDto) {
        Member member = utilService.getSignInMember();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply build = Reply.builder().member(member).content(replyWriteDto.getContent()).build();

        comment.addReply(build);
        //replyRepository.save(reply);

        addMentionsToReply(build, replyWriteDto.getMentions());

        return build.getId();
    }

    @Override
    public Long updateReply(Long replyId, ReplyRequest.Post replyWriteDto) {
        Reply reply = getReplyById(replyId);
        reply.changeContent(replyWriteDto.getContent());
        reply.getMentions().clear();
        addMentionsToReply(reply, replyWriteDto.getMentions());
        return reply.getId();
    }

    @Override
    public void deleteReply(Long replyId) {
        Reply reply = getReplyById(replyId);
        if (!replyQueryService.validateMember(replyId)) {
            throw new MemberHandler(ErrorStatus.CANNOT_TOUCH_NOT_YOURS);
        }
        replyRepository.delete(reply);
    }

    private Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyHandler(ErrorStatus.REPLY_NOT_FOUND));
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
