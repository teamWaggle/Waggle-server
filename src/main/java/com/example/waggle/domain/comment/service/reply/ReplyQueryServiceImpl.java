package com.example.waggle.domain.comment.service.reply;

import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.reply.ReplyViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.global.exception.ErrorCode.REPLY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReplyQueryServiceImpl implements ReplyQueryService{
    private final ReplyRepository replyRepository;
    private final UtilService utilService;

    @Override
    public List<ReplyViewDto> getReplies(Long commentId) {
        List<Reply> replies = replyRepository.findByCommentId(commentId);
        return replies.stream().map(ReplyViewDto::toDto).collect(Collectors.toList());
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
}
