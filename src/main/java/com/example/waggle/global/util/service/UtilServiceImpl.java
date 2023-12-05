package com.example.waggle.global.util.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.domain.board.question.repository.AnswerRepository;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.hashtag.entity.Hashtag;
import com.example.waggle.domain.hashtag.repository.HashtagRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.handler.*;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {

    private final MemberRepository memberRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final HelpRepository helpURepository;
    private final HashtagRepository hashtagRepository;

    @Override
    @Transactional(readOnly = true)
    public Member getMember(String username) {
        //member setting
        Member signInMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return signInMember;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateMemberUseBoard(Long boardId, BoardType boardType) {
        Member signInMember = getSignInMember();
        //board get
        Board board;

        switch (boardType) {
            case STORY:
                board = storyRepository.findById(boardId)
                        .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case QUESTION:
                board = questionRepository.findById(boardId)
                        .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case ANSWER:
                board = answerRepository.findById(boardId)
                        .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case HELP:
                board = helpURepository.findById(boardId)
                        .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            default:
                // error: Invalid dtype
                throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
        }
        return board.getMember().equals(signInMember);
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
        throw new MemberHandler(ErrorStatus.MEMBER_REFRESH_TOKEN_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Board getBoard(Long boardId, BoardType boardType) {
        //board get
        Board board;

        switch (boardType) {
            case STORY:
                board = storyRepository.findById(boardId)
                        .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case QUESTION:
                board = questionRepository.findById(boardId)
                        .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case ANSWER:
                board = answerRepository.findById(boardId)
                        .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case HELP:
                board = helpURepository.findById(boardId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
                break;
            default:
                // error: Invalid dtype
                throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
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
