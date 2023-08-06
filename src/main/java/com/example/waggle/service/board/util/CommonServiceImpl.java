package com.example.waggle.service.board.util;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.member.Member;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService{

    private final MemberRepository memberRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    @Override
    public Member getMember(String username) {
        //member get
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            //error
            return null;
        }
        Member member = byUsername.get();
        return member;
    }

    @Override
    public boolean login() {
        if (SecurityUtil.getCurrentUsername().equals("anonymousUser")) {
            return false;
        }
        return true;
    }

    @Override
    public Member getSignInMember() {
        Member signInMember = null;

        //check login
        if (login()) {
            //check exist user
            signInMember = getMember(SecurityUtil.getCurrentUsername());
        }
        return signInMember;
    }

    @Override
    public Board getBoard(Long boardId, String boardType) {
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
