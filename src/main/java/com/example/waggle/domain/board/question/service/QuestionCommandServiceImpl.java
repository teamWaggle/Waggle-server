package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
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
    private final UtilService utilService;

    @Override
    public Long createQuestion(QuestionRequest.QuestionWriteDto request, List<String> uploadedFiles) {
        Member member = utilService.getSignInMember();

        Question createdQuestion = Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member).build();

        Question question = questionRepository.save(createdQuestion);

        if (!request.getHashtags().isEmpty()) {
            for (String hashtag : request.getHashtags()) {
                utilService.saveHashtag(question, hashtag);
            }
        }

//        mediaService.createMedias(question.getId(), multipartFiles, QUESTION);
        return question.getId();
    }

    @Override
    public Long updateQuestion(Long boardId, QuestionRequest.QuestionWriteDto request, List<String> uploadedFiles) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        question.changeQuestion(request.getContent(), request.getTitle());

        question.getMedias().clear();
//        mediaService.createMedias(question.getId(), multipartFiles, QUESTION);

        question.getBoardHashtags().clear();
        for (String hashtag : request.getHashtags()) {
            utilService.saveHashtag(question, hashtag);
        }
        return question.getId();
    }


    @Override
    public void deleteQuestion(Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        if (!utilService.validateMemberUseBoard(boardId, BoardType.QUESTION)) {
            throw new MemberHandler(ErrorStatus.CANNOT_TOUCH_NOT_YOURS);
        }
        questionRepository.delete(question);
    }



}
