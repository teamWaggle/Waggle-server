package com.example.waggle.domain.board.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.hashtag.entity.Hashtag;
import com.example.waggle.domain.hashtag.repository.HashtagRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.handler.*;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SirenRepository sirenRepository;
    private final ScheduleRepository scheduleRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberScheduleRepository memberScheduleRepository;
    private final MemberQueryService memberQueryService;


    @Override
    public boolean validateMemberUseBoard(Long boardId, BoardType boardType) {
        Member signInMember = memberQueryService.getSignInMember();

        Board board = findBoard(boardId, boardType);
        return board.getMember().equals(signInMember);
    }


    @Override
    public boolean validateMemberUseBoard(Long boardId, BoardType boardType, Member member) {
        Board board = findBoard(boardId, boardType);
        return board.getMember().equals(member);
    }


    @Override
    public void saveHashtag(Board board, String tag) {
        Hashtag hashtag = getHashtag(tag);

        BoardHashtag.builder()
                .hashtag(hashtag)
                .board(board)
                .build()
                .link(board, hashtag);
    }

    @Override
    public Hashtag getHashtag(String tag) {
        Optional<Hashtag> byContent = hashtagRepository.findByContent(tag);
        if (byContent.isEmpty()) {
            Hashtag build = Hashtag.builder().content(tag).build();
            hashtagRepository.save(build);
            return build;
        }
        return byContent.get();
    }

    private Board findBoard(Long boardId, BoardType boardType) {
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
            case SIREN:
                board = sirenRepository.findById(boardId)
                        .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case SCHEDULE:
                board = scheduleRepository.findById(boardId)
                        .orElseThrow(() -> new ScheduleHandler(ErrorStatus.BOARD_NOT_FOUND));
                break;
            default:
                throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
        }
        return board;
    }
}
