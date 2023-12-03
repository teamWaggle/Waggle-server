package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.answer.AnswerWriteDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest.QuestionWriteDto request, List<String> uploadedFiles);

    Long createAnswer(Long boardId, AnswerWriteDto answerWriteDto, List<MultipartFile> multipartFiles) throws IOException;

    Long updateQuestion(Long boardId, QuestionRequest.QuestionWriteDto request, List<String> uploadedFiles);

    Long updateAnswer(Long boardId, AnswerWriteDto answerWriteDto, List<MultipartFile> multipartFiles) throws IOException;

    boolean validateMember(Long boardId, String boardType);

    void deleteQuestion(Long boardId);

    void deleteAnswer(Long boardId);
}
