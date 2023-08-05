package com.example.waggle.service.board;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.comment.MemberMention;
import com.example.waggle.domain.board.comment.Reply;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.comment.CommentViewDto;
import com.example.waggle.dto.board.reply.ReplyViewDto;
import com.example.waggle.dto.board.reply.ReplyWriteDto;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import com.example.waggle.repository.board.comment.ReplyRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReplyService {

    private final MemberRepository memberRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;


    //1. 조회
    @Transactional(readOnly = true)
    public List<ReplyViewDto> findReplies (Long commentId) {
        Optional<Comment> commentById = commentRepository.findById(commentId);
        if (commentById.isEmpty()) {
            log.info("not exist comment");
            //error
            return null;
        }
        Comment comment = commentById.get();
        return comment.getReplies().stream()
                .map(ReplyViewDto::toDto).collect(Collectors.toList());
    }
    //2. 저장
    public Long saveReply(CommentViewDto commentViewDto, ReplyWriteDto replyWriteDto) {
        Member member = getMember(SecurityUtil.getCurrentUsername());

        //check exist
        Optional<Comment> commentById = commentRepository.findById(commentViewDto.getId());
        if (commentById.isEmpty()) {
            log.info("not exist comment");
            //error
            return null;
        }
        Comment comment = commentById.get();

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
        Member member = getMember(SecurityUtil.getCurrentUsername());
        Optional<Reply> replyById = replyRepository.findById(viewDto.getId());
        if (replyById.isEmpty()) {
            log.info("not exist comment");
            //error
            return false;
        }
        Reply reply = replyById.get();
        return reply.getMember().equals(member);
    }
    public Long changeReply(ReplyViewDto replyViewDto, ReplyWriteDto replyWriteDto) {
        //find
        Optional<Reply> replyById = replyRepository.findById(replyViewDto.getId());
        if (replyById.isEmpty()) {
            log.info("not exist reply");
            //error
            return null;
        }
        Reply reply = replyById.get();

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
        Member member = getMember(SecurityUtil.getCurrentUsername());
        Optional<Reply> replyById = replyRepository.findById(viewDto.getId());
        if (replyById.isEmpty()) {
            log.info("not exist reply");
            //error
            return;
        }
        if (replyById.get().getMember().equals(member)) {
            log.info("delete completely!");
            replyRepository.delete(replyById.get());
        }
    }

    //else
    /**
     * use at :
     * @param username
     * @return member
     */
    private Member getMember(String username) {
        //member setting
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            //error
            return null;
        }
        Member signInMember = byUsername.get();
        return signInMember;
    }
    private Board getBoard(Long boardId, String boardType) {
        //board get
        Board board;

        switch (boardType) {
            case "story":
                Optional<Story> storyById = storyRepository.findById(boardId);
                if (storyById.isEmpty()) {
                    //error
                }
                board = storyById.get();
                break;
            case "question":
                Optional<Question> questionById = questionRepository.findById(boardId);
                if (questionById.isEmpty()) {
                    //error
                }
                board = questionById.get();
                break;
            case "answer":
                Optional<Answer> answerById = answerRepository.findById(boardId);
                if (answerById.isEmpty()) {
                    //error
                }
                board = answerById.get();
                break;
            default:
                // error: Invalid dtype
                return null;
        }
        return board;
    }
}
