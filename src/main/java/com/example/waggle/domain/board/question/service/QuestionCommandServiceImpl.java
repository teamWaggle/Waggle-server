package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.web.dto.question.QuestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RecommendRepository recommendRepository;
    private final MemberQueryService memberQueryService;
    private final AnswerCommandService answerCommandService;
    private final BoardService boardService;



    @Override
    public Long createQuestion(QuestionRequest.QuestionWriteDto request) {
        Member member = memberQueryService.getSignInMember();

        Question createdQuestion = Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member).build();

        Question question = questionRepository.save(createdQuestion);

        for (String hashtag : request.getHashtags()) {
            boardService.saveHashtag(question, hashtag);
        }

//        mediaService.createMedias(question.getId(), multipartFiles, QUESTION);
        return question.getId();
    }

    @Override
    public Long updateQuestion(Long boardId, QuestionRequest.QuestionWriteDto request) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        question.changeQuestion(request.getContent(), request.getTitle());

        question.getMedias().clear();
//        mediaService.createMedias(question.getId(), multipartFiles, QUESTION);

        question.getBoardHashtags().clear();
        for (String hashtag : request.getHashtags()) {
            boardService.saveHashtag(question, hashtag);
        }
        return question.getId();
    }

    @Override
    public void deleteQuestion(Long boardId) {
        if (!boardService.validateMemberUseBoard(boardId, BoardType.QUESTION)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<Answer> answers = answerRepository.findAnswerByQuestionId(question.getId());
        answers.stream().forEach(a -> answerCommandService.deleteAnswer(a.getId()));

        List<Recommend> recommends = recommendRepository.findByBoardId(question.getId());
        recommends.stream().forEach(r -> recommendRepository.delete(r));


        questionRepository.delete(question);
    }

}
