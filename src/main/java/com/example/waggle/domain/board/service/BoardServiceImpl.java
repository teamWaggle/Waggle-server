package com.example.waggle.domain.board.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.board.siren.service.SirenCommandService;
import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.hashtag.entity.Hashtag;
import com.example.waggle.domain.hashtag.repository.HashtagRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.service.ScheduleCommandService;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.handler.*;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final MemberQueryService memberQueryService;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SirenRepository sirenRepository;
    private final ScheduleRepository scheduleRepository;
    private final HashtagRepository hashtagRepository;
    private final StoryCommandService storyCommandService;
    private final QuestionCommandService questionCommandService;
    private final AnswerCommandService answerCommandService;
    private final SirenCommandService sirenCommandService;
    private final ScheduleCommandService scheduleCommandService;


    @Override
    public boolean validateMemberUseBoard(Long boardId, BoardType boardType) {
        Member signInMember = memberQueryService.getSignInMember();

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
            default:
                throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
        }
        return board.getMember().equals(signInMember);
    }

    @Override
    public void deleteAllBoardByMember(String username) {
        List<Story> stories = storyRepository.findListByMemberUsername(username);
        List<Question> questions = questionRepository.findListByMemberUsername(username);
        List<Answer> answers = answerRepository.findListByMemberUsername(username);
        List<Siren> sirens = sirenRepository.findListByMemberUsername(username);
        List<Schedule> schedules = scheduleRepository.findListByMemberUsername(username);

        stories.forEach(story -> storyCommandService.deleteStory(story.getId()));
        questions.forEach(question -> questionCommandService.deleteQuestion(question.getId()));
        answers.forEach(answer -> answerCommandService.deleteAnswer(answer.getId()));
        sirens.forEach(siren -> sirenCommandService.deleteSiren(siren.getId()));
        schedules.forEach(schedule -> scheduleCommandService.deleteSchedule(schedule.getId()));
    }

    @Override
    public Board getBoard(Long boardId, BoardType boardType) {
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
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
                break;
            case SCHEDULE:
                board = scheduleRepository.findById(boardId)
                        .orElseThrow(() -> new ScheduleHandler(ErrorStatus.BOARD_NOT_FOUND));
            default:
                throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
        }
        return board;
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
}
