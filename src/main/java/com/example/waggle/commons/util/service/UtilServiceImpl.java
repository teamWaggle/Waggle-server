package com.example.waggle.commons.util.service;

import com.example.waggle.commons.security.SecurityUtil;
import com.example.waggle.board.Board;
import com.example.waggle.hashtag.domain.BoardHashtag;
import com.example.waggle.hashtag.domain.Hashtag;
import com.example.waggle.member.domain.Member;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.exception.ErrorCode;
import com.example.waggle.hashtag.repository.HashtagRepository;
import com.example.waggle.board.question.repository.AnswerRepository;
import com.example.waggle.board.question.repository.QuestionRepository;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.waggle.commons.exception.ErrorCode.*;

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
                .orElseThrow(() -> new CustomPageException(ErrorCode.MEMBER_NOT_FOUND));

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
        throw new CustomPageException(REFRESH_TOKEN_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Board getBoard(Long boardId, BoardType boardType) {
        //board get
        Board board;

        switch (boardType) {
            case STORY:
                board = storyRepository.findById(boardId)
                        .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
                break;
            case QUESTION:
                board = questionRepository.findById(boardId)
                        .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
                break;
            case ANSWER:
                board = answerRepository.findById(boardId)
                        .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
                break;
            default:
                // error: Invalid dtype
                throw new CustomPageException(INVALID_BOARD_TYPE);
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
