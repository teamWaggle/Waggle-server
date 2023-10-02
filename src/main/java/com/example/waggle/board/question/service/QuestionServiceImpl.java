package com.example.waggle.board.question.service;


import com.example.waggle.board.question.domain.Answer;
import com.example.waggle.board.question.domain.Question;
import com.example.waggle.board.question.dto.*;
import com.example.waggle.board.question.repository.AnswerRepository;
import com.example.waggle.board.question.repository.QuestionRepository;
import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.exception.ErrorCode;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.media.domain.Media;
import com.example.waggle.member.domain.Member;
import com.example.waggle.schedule.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.*;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UtilService utilService;


    @Override
    public List<QuestionSummaryDto> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(QuestionSummaryDto::toDto).collect(Collectors.toList());
    }

    @Override
    public List<QuestionSummaryDto> getQuestionsByUsername(String username) {
        List<Question> questions = questionRepository.findByMemberUsername(username);
        return questions.stream().map(QuestionSummaryDto::toDto).collect(Collectors.toList());
    }

    @Override
    public QuestionDetailDto getQuestionByBoardId(Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));


        List<AnswerDetailDto> answerDetailDtos = question.getAnswers().stream().map(AnswerDetailDto::toDto).collect(Collectors.toList());

        QuestionDetailDto questionDetailDto = QuestionDetailDto.toDto(question);
        questionDetailDto.linkAnswerView(answerDetailDtos);

        return questionDetailDto;
    }

    @Transactional
    @Override
    public Long createQuestion(QuestionWriteDto questionWriteDto) {
        Member signInMember = utilService.getSignInMember();
        Question question = questionWriteDto.toEntity(signInMember);
        questionRepository.save(question);

        if (!questionWriteDto.getHashtags().isEmpty()) {
            for (String hashtag : questionWriteDto.getHashtags()) {
                utilService.saveHashtag(question, hashtag);
            }
        }

        if (!questionWriteDto.getMedias().isEmpty()) {
            for (String media : questionWriteDto.getMedias()) {
                Media.builder().url(media).board(question).build().linkBoard(question);
            }
        }
        return question.getId();
    }

    @Transactional
    @Override
    public Long createAnswer(AnswerWriteDto answerWriteDto, Long boardId) {
        Member signInMember = utilService.getSignInMember();
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        Answer answer = answerWriteDto.toEntity(signInMember);

        if (!answerWriteDto.getHashtags().isEmpty()) {
            for (String hashtag : answerWriteDto.getHashtags()) {
                utilService.saveHashtag(answer, hashtag);
            }
        }

        if (!answerWriteDto.getMedias().isEmpty()) {
            for (String media : answerWriteDto.getMedias()) {
                Media.builder().url(media).board(answer).build().linkBoard(answer);
            }
        }
        question.addAnswer(answer);

        return answer.getId();
    }

    @Transactional
    @Override
    public Long updateQuestion(QuestionWriteDto questionWriteDto, Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        question.changeQuestion(questionWriteDto.getContent(), questionWriteDto.getTitle());

        question.getMedias().clear();

        for (String media : questionWriteDto.getMedias()) {
            Media.builder().url(media).board(question).build().linkBoard(question);
        }

        question.getBoardHashtags().clear();

        for (String hashtag : questionWriteDto.getHashtags()) {
            utilService.saveHashtag(question, hashtag);
        }
        return question.getId();
    }

    @Transactional
    @Override
    public Long updateAnswer(AnswerWriteDto answerWriteDto, Long boardId) {
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        answer.changeAnswer(answerWriteDto.getContent());

        answer.getMedias().clear();

        for (String media : answerWriteDto.getMedias()) {
            Media.builder().url(media).board(answer).build();
        }

        answer.getBoardHashtags().clear();

        for (String hashtag : answerWriteDto.getHashtags()) {
            utilService.saveHashtag(answer, hashtag);
        }

        return answer.getId();
    }

    @Override
    public boolean validateMember(Long boardId, String boardType) {
        Member member = utilService.getSignInMember();
        boolean isSameUser;
        switch (boardType) {
            case "question":
                Question question = questionRepository.findById(boardId)
                        .orElseThrow(() -> new CustomAlertException(BOARD_NOT_FOUND));
                isSameUser = question.getMember().equals(member);
                break;
            case "answer":
                Answer answer = answerRepository.findById(boardId)
                        .orElseThrow(() -> new CustomAlertException(BOARD_NOT_FOUND));
                isSameUser = answer.getMember().equals(member);
                break;
            default:
                throw new CustomPageException(INVALID_BOARD_TYPE);
        }
        return isSameUser;
    }

    @Transactional
    @Override
    public void deleteQuestion(Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        if (!validateMember(boardId, "question")) {
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }

        questionRepository.delete(question);
    }

    @Transactional
    @Override
    public void deleteAnswer(Long boardId) {
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        if (!validateMember(boardId, "answer")) {
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }
        answerRepository.delete(answer);
    }

}
