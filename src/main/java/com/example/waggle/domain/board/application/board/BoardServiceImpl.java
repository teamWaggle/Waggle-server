package com.example.waggle.domain.board.application.board;

import com.example.waggle.domain.board.persistence.dao.question.jpa.QuestionRepository;
import com.example.waggle.domain.board.persistence.dao.siren.jpa.SirenRepository;
import com.example.waggle.domain.board.persistence.dao.story.jpa.StoryRepository;
import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.domain.hashtag.persistence.dao.HashtagRepository;
import com.example.waggle.domain.hashtag.persistence.entity.BoardHashtag;
import com.example.waggle.domain.hashtag.persistence.entity.Hashtag;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.dao.jpa.ScheduleRepository;
import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.object.handler.QuestionHandler;
import com.example.waggle.exception.object.handler.ScheduleHandler;
import com.example.waggle.exception.object.handler.SirenHandler;
import com.example.waggle.exception.object.handler.StoryHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
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
    private final SirenRepository sirenRepository;
    private final ScheduleRepository scheduleRepository;
    private final HashtagRepository hashtagRepository;


    @Override
    public boolean validateMemberUseBoard(Long boardId, BoardType boardType, Member member) {
        Board board = findBoard(boardId, boardType);        //TODO remove private method because of not necessary
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
