package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.question.QuestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final MediaCommandService mediaCommandService;


    @Override
    public Long createQuestion(QuestionRequest.Post request,
                               List<MultipartFile> multipartFiles) throws IOException {
        Question createdQuestion = buildQuestion(request);
        Question question = questionRepository.save(createdQuestion);

        for (String hashtag : request.getHashtags()) {
            boardService.saveHashtag(question, hashtag);
        }
        mediaCommandService.createMedia(multipartFiles, question);
        return question.getId();
    }


    @Override
    public Long updateQuestion(Long boardId,
                               QuestionRequest.Post request,
                               List<MultipartFile> multipartFiles,
                               List<String> deleteFiles) throws IOException {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));


        mediaCommandService.updateMedia(multipartFiles, deleteFiles, question);

        question.getBoardHashtags().clear();
        for (String hashtag : request.getHashtags()) {
            boardService.saveHashtag(question, hashtag);
        }
        return question.getId();
    }

    @Override
    public Long updateQuestionV2(Long boardId,
                                 QuestionRequest.Put questionUpdateDto,
                                 MediaRequest.Put mediaUpdateDto,
                                 List<MultipartFile> multipartFiles) throws IOException {
        if (!SecurityUtil.getCurrentUsername().equals(questionUpdateDto.getUsername())) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));


        question.changeQuestion(questionUpdateDto);

        mediaCommandService.updateMediaV2(mediaUpdateDto, multipartFiles, question);

        question.getBoardHashtags().clear();
        for (String hashtag : questionUpdateDto.getHashtags()) {
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

    private Question buildQuestion(QuestionRequest.Post request) {
        Member member = memberQueryService.getSignInMember();

        Question createdQuestion = Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(request.getStatus())
                .member(member).build();
        return createdQuestion;
    }
}
