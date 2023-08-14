package com.example.waggle.service.board.util;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.member.Member;
import com.example.waggle.exception.CustomException;
import com.example.waggle.exception.ErrorCode;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.waggle.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UtilServiceImpl implements UtilService {

    private final MemberRepository memberRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    @Transactional(readOnly = true)
    public Member getMember(String username) {
        //member setting
        Member signInMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return signInMember;
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
        //check login
        if (login()) {
            //check exist user
            Member signInMember = getMember(SecurityUtil.getCurrentUsername());
            return signInMember;
        }
        throw new CustomException(REFRESH_TOKEN_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Board getBoard(Long boardId, BoardType boardType) {
        //board get
        Board board;

        switch (boardType) {
            case STORY:
                Optional<Story> storyById = storyRepository.findById(boardId);
                if (storyById.isEmpty()) {
                    //error
                }
                board = storyById.get();
                break;
            case QUESTION:
                Optional<Question> questionById = questionRepository.findById(boardId);
                if (questionById.isEmpty()) {
                    //error
                }
                board = questionById.get();
                break;
            case ANSWER:
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

    @Override
    public void saveHashtag(Board board, String hashtag) {
        Optional<Hashtag> hashtagByTag = hashtagRepository.findByTag(hashtag);
        if (hashtagByTag.isEmpty()) {
            log.info("not exist hashtag, so newly making");
            Hashtag buildHashtag = Hashtag.builder().tag(hashtag).build();
            hashtagRepository.save(buildHashtag);
            BoardHashtag.builder()
                    .hashtag(buildHashtag)
                    .board(board)
                    .build()
                    .link(board, buildHashtag);
        }//아래 else가 좀 반복되는 것 같다...
        else{
            log.info("already exist hashtag");
            BoardHashtag.builder()
                    .hashtag(hashtagByTag.get())
                    .board(board)
                    .build()
                    .link(board, hashtagByTag.get());
        }
    }
}
