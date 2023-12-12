package com.example.waggle.domain.comment.service.reply;

import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.global.exception.handler.ReplyHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReplyQueryServiceImpl implements ReplyQueryService{
    private final ReplyRepository replyRepository;
    private final BoardService utilService;

    @Override
    public List<Reply> getReplies(Long commentId) {
        List<Reply> replies = replyRepository.findByCommentId(commentId);
        return replies;
    }

    @Override
    public Page<Reply> getPagedReplies(Long commentId, Pageable pageable) {
        return replyRepository.findPagedReplyByCommentId(commentId, pageable);
    }



    private Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyHandler(ErrorStatus.REPLY_NOT_FOUND));
    }
}
