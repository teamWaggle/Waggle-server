package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Answer;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.AnswerRepository;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.CustomAlertException;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.answer.AnswerWriteDto;
import com.example.waggle.web.dto.question.QuestionWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UtilService utilService;

    @Override
    public Long createQuestion(QuestionWriteDto questionWriteDto, List<MultipartFile> multipartFiles) throws IOException {
        Member signInMember = utilService.getSignInMember();
        Question saveQuestion = questionWriteDto.toEntity(signInMember);
        Question question = questionRepository.save(saveQuestion);

        if (!questionWriteDto.getHashtags().isEmpty()) {
            for (String hashtag : questionWriteDto.getHashtags()) {
                utilService.saveHashtag(question, hashtag);
            }
        }

//        mediaService.createMedias(question.getId(), multipartFiles, QUESTION);
        return question.getId();
    }

    @Override
    public Long createAnswer(Long boardId, AnswerWriteDto answerWriteDto, List<MultipartFile> multipartFiles) throws IOException {
        Member signInMember = utilService.getSignInMember();
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        Answer saveAnswer = answerWriteDto.toEntity(signInMember);
        Answer answer = answerRepository.save(saveAnswer);

        if (!answerWriteDto.getHashtags().isEmpty()) {
            for (String hashtag : answerWriteDto.getHashtags()) {
                utilService.saveHashtag(answer, hashtag);
            }
        }

//        mediaService.createMedias(answer.getId(), multipartFiles, ANSWER);
        question.addAnswer(answer);

        return answer.getId();
    }

    @Override
    public Long updateQuestion(Long boardId, QuestionWriteDto questionWriteDto, List<MultipartFile> multipartFiles) throws IOException {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        question.changeQuestion(questionWriteDto.getContent(), questionWriteDto.getTitle());

        question.getMedias().clear();
//        mediaService.createMedias(question.getId(), multipartFiles, QUESTION);

        question.getBoardHashtags().clear();
        for (String hashtag : questionWriteDto.getHashtags()) {
            utilService.saveHashtag(question, hashtag);
        }
        return question.getId();
    }

    @Override
    public Long updateAnswer(Long boardId, AnswerWriteDto answerWriteDto, List<MultipartFile> multipartFiles) throws IOException {
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        answer.changeAnswer(answerWriteDto.getContent());

        answer.getMedias().clear();
//        mediaService.createMedias(answer.getId(), multipartFiles, ANSWER);

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

    @Override
    public void deleteQuestion(Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        if (!validateMember(boardId, "question")) {
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }

        questionRepository.delete(question);
    }

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
