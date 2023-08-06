package com.example.waggle.service.board;

import com.example.waggle.component.jwt.SecurityUtil;
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
        List<Comment> commentsByBoardId = commentRepository.findByBoardId(boardId);
        return commentsByBoardId.stream().map(CommentViewDto::toDto).collect(Collectors.toList());
    }

    //2. 저장
    public Long saveComment(Long boardId, CommentWriteDto writeDto, String boardType) {
        Member member = getSignInMember();
        Board board = getBoard(boardId, boardType);
        Comment saveComment = writeDto.toEntity(member, board);
        Comment save = commentRepository.save(saveComment);
        return save.getId();
    }

    //3. 수정
    // question
    public Long editComment(CommentViewDto viewDto, CommentWriteDto writeDto) {
        //check exist comment
        Optional<Comment> commentById = commentRepository.findById(viewDto.getId());
        if (commentById.isEmpty()) {
            log.info("not exist comment");
            //error
            return null;
        }
        Comment comment = commentById.get();

        //edit
        comment.changeContent(writeDto.getContent());

        return comment.getId();
    }


    //3.1 check member
    public boolean checkMember(CommentViewDto viewDto) {
        Member member = getSignInMember();
        Optional<Comment> commentById = commentRepository.findById(viewDto.getId());
        if (commentById.isEmpty()) {
            log.info("not exist comment");
            //error
            return false;
        }
        Comment comment = commentById.get();
        return comment.getMember().equals(member);
    }

    // 4. 삭제
    // 아래에서 따로 멤버 검증이 없는 이유는
    // 어차피 레포지토리에서 데이터를 가져올 때 멤버를 필터로 사용하기 때문이다.
    public void deleteComment(CommentViewDto viewDto) {
        Member member = getSignInMember();
        //check exist comment
        Optional<Comment> commentById = commentRepository.findById(viewDto.getId());
        if (commentById.isEmpty()) {
            log.info("not exist comment");
            //error
            return;
        }
        Comment comment = commentById.get();
        if (comment.getMember().equals(member)) {
            log.info("delete completely!");
            commentRepository.delete(comment);
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
            log.info("can't find user!");
            //error
            //여기서 return null 이 아니라 위 로직이 아예 멈출 수 있도록 한다.
            return null;
        }
        Member signInMember = byUsername.get();
        log.info("signInMember user name is {}", signInMember.getUsername());
        return signInMember;
    }

    private boolean login() {
        if (SecurityUtil.getCurrentUsername().equals("anonymousUser")) {
            return false;
        }
        return true;
    }

    private Member getSignInMember() {
        Member signInMember = null;

        //check login
        if (login()) {
            //check exist user
            signInMember = getMember(SecurityUtil.getCurrentUsername());
        }
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
