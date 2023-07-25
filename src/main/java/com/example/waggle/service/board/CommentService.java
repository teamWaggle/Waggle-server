package com.example.waggle.service.board;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.comment.CommentViewDto;
import com.example.waggle.dto.board.comment.CommentWriteDto;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final MemberRepository memberRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;

    //1. 조회
    //p1. 나중에 생성 역순으로 나열해야함
    public List<CommentViewDto> findComments(Long boardId) {
        List<Comment> commentsByBoardId = commentRepository.findCommentsByBoardId(boardId);
        return commentsByBoardId.stream().map(CommentViewDto::toDto).collect(Collectors.toList());
    }

    //2. 저장
    public Long saveComment(Long boardId, String username, CommentWriteDto writeDto, String boardType) {
        Member member = getMember(username);
        Board board = getBoard(boardId, boardType);

        Comment saveComment = Comment.builder()
                .content(writeDto.getContent())
                .board(board)
                .member(member)
                .build();

        Comment save = commentRepository.save(saveComment);
        return save.getId();
    }

    //3. 수정
    // question
    public Long editCommentV1(String username, CommentViewDto viewDto, CommentWriteDto writeDto) {
        Member member = getMember(username);
        //check exist comment
        Optional<Comment> commentById = commentRepository.findById(viewDto.getId());
        if (commentById.isEmpty()) {
            log.info("not exist comment");
            //error
            return null;
        }
        Comment comment = commentById.get();

        //check user
        if (!comment.getMember().equals(member)) {
            log.info("only same user can edit comment");
        }

        //edit
        comment.changeContent(writeDto.getContent());

        return comment.getId();
    }
    public Long editCommentV2(String username, Long boardId, CommentWriteDto writeDto) {
        Member member = getMember(username);

        //check exist comment
        Optional<Comment> commentById = commentRepository.findCommentByMemberAndBoardId(member, boardId);
        if (commentById.isEmpty()) {
            log.info("not exist comment");
            //error
            return null;
        }
        Comment comment = commentById.get();

        //check user
        if (!comment.getMember().equals(member)) {
            log.info("only same user can edit comment");
            //return null;
        }

        //edit
        comment.changeContent(writeDto.getContent());
        return comment.getId();

    }

    //4. 삭제
    public void deleteComment(String username, CommentViewDto viewDto) {
        Member member = getMember(username);
        Optional<Comment> commentByMemberAndBoardId = commentRepository
                .findCommentByMemberAndBoardId(member, viewDto.getId());
        if (commentByMemberAndBoardId.isEmpty()) {
            log.info("not exist comment");
            //error
            return;
        }
        Comment comment = commentByMemberAndBoardId.get();
        commentRepository.delete(comment);
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
