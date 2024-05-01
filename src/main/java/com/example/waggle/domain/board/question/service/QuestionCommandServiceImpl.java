package com.example.waggle.domain.board.question.service;

import static com.example.waggle.domain.board.service.BoardType.QUESTION;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.question.QuestionRequest;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RecommendRepository recommendRepository;
    private final AnswerCommandService answerCommandService;
    private final BoardService boardService;
    private final MediaCommandService mediaCommandService;
    private final RedisService redisService;

    @Override
    public Long createQuestion(QuestionRequest createQuestionRequest,
                               Member member) {
        Question createdQuestion = buildQuestion(createQuestionRequest, member);
        Question question = questionRepository.save(createdQuestion);

        for (String hashtag : createQuestionRequest.getHashtagList()) {
            boardService.saveHashtag(question, hashtag);
        }
        mediaCommandService.createMedia(createQuestionRequest.getMediaList(), question);
        return question.getId();
    }

    @Override
    public Long updateQuestion(Long boardId,
                               QuestionRequest updateQuestionRequest,
                               Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        question.changeQuestion(updateQuestionRequest);
        mediaCommandService.updateMedia(updateQuestionRequest.getMediaList(), question);

        question.getBoardHashtags().clear();
        for (String hashtag : updateQuestionRequest.getHashtagList()) {
            boardService.saveHashtag(question, hashtag);
        }
        return question.getId();
    }


    @Override
    public void convertStatus(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        switch (question.getStatus()) {
            case RESOLVED -> question.changeStatus(ResolutionStatus.UNRESOLVED);
            case UNRESOLVED -> question.changeStatus(ResolutionStatus.RESOLVED);
            default -> throw new QuestionHandler(ErrorStatus.BOARD_INVALID_TYPE);
        }
    }

    @Override
    public void deleteQuestion(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<Answer> answers = answerRepository.findAnswerByQuestionId(question.getId());
        answers.stream().forEach(answer -> answerCommandService.deleteAnswer(answer.getId(), member));
        recommendRepository.deleteAllByBoardId(question.getId());
        questionRepository.delete(question);
    }

    @Override
    public void increaseQuestionViewCount(Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        question.increaseViewCount();
    }

    @Override
    public void applyViewCountToRedis(Long boardId) {
        String viewCountKey = "viewCount::" + boardId;
        if (redisService.getValue(viewCountKey) != null) {
            redisService.increment(viewCountKey);
            return;
        }
        redisService.setData(
                viewCountKey,
                String.valueOf(questionRepository.findViewCountByBoardId(boardId) + 1),
                Duration.ofMinutes(3)
        );
    }

    @Scheduled(cron = "0 0/3 * * * ?")
    @Override
    public void applyViewCountToRDB() {
        Set<String> viewCountKeys = redisService.keys("viewCount*");
        if (Objects.requireNonNull(viewCountKeys).isEmpty()) {
            return;
        }

        for (String viewCntKey : viewCountKeys) {
            Long boardId = extractBoardIdFromKey(viewCntKey);
            Long viewCount = Long.parseLong(redisService.getData(viewCntKey));
            questionRepository.applyViewCntToRDB(boardId, viewCount);
            redisService.deleteData(viewCntKey);
            redisService.deleteData("board::" + boardId);
        }
    }

    private static Long extractBoardIdFromKey(String key) {
        return Long.parseLong(key.split("::")[1]);
    }

    private Question buildQuestion(QuestionRequest createQuestionRequest, Member member) {
        return Question.builder()
                .title(createQuestionRequest.getTitle())
                .content(createQuestionRequest.getContent())
                .status(ResolutionStatus.UNRESOLVED)
                .member(member)
                .build();
    }
}
