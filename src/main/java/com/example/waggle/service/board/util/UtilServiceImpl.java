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

import static com.example.waggle.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
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
    @Transactional(readOnly = true)
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
                board = storyRepository.findById(boardId)
                        .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
                break;
            case QUESTION:
                board = questionRepository.findById(boardId)
                        .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
                break;
            case ANSWER:
                board = answerRepository.findById(boardId)
                        .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
                break;
            default:
                // error: Invalid dtype
                throw new CustomException(INVALID_BOARD_TYPE);
        }
        return board;
    }

    @Override
    @Transactional
    public void saveHashtag(Board board, String tag) {
        Hashtag hashtag = getHashtag(tag);

        BoardHashtag.builder()
                .hashtag(hashtag)
                .board(board)
                .build()
                .link(board, hashtag);
    }

    @Override
    @Transactional
    public Hashtag getHashtag(String tag) {
        Optional<Hashtag> byTag = hashtagRepository.findByTag(tag);
        if (byTag.isEmpty()) {
            log.info("not exist hashtag!");
            Hashtag build = Hashtag.builder().tag(tag).build();
            hashtagRepository.save(build);
            return build;
        }
        log.info("already exist hashtag...");
        return byTag.get();
    }
}
