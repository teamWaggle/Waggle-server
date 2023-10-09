package com.example.waggle.board.question.service;


import com.example.waggle.board.question.dto.AnswerWriteDto;
import com.example.waggle.board.question.dto.QuestionDetailDto;
import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.dto.QuestionWriteDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionService {
    List<QuestionSummaryDto> getQuestions();

    List<QuestionSummaryDto> getQuestionsByUsername(String username);

    QuestionDetailDto getQuestionByBoardId(Long boardId);

    Long createQuestion(QuestionWriteDto questionWriteDto, List<MultipartFile> multipartFiles) throws IOException;

    Long createAnswer(AnswerWriteDto answerWriteDto, Long boardId, List<MultipartFile> multipartFiles) throws IOException;

    Long updateQuestion(QuestionWriteDto questionWriteDto, Long boardId, List<MultipartFile> multipartFiles) throws IOException;

    Long updateAnswer(AnswerWriteDto answerWriteDto, Long boardId, List<MultipartFile> multipartFiles) throws IOException;

    boolean validateMember(Long boardId, String boardType);

    void deleteQuestion(Long boardId);

    void deleteAnswer(Long boardId);
}
