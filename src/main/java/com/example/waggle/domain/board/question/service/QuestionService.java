package com.example.waggle.domain.board.question.service;


import com.example.waggle.web.dto.answer.AnswerWriteDto;
import com.example.waggle.web.dto.question.QuestionDetailDto;
import com.example.waggle.web.dto.question.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionWriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionService {
    List<QuestionSummaryDto> getQuestions();

    Page<QuestionSummaryDto> getPagedQuestionsByUsername(String username,Pageable pageable);

    QuestionDetailDto getQuestionByBoardId(Long boardId);

    Page<QuestionSummaryDto> getPagedQuestions(Pageable pageable);

    Long createQuestion(QuestionWriteDto questionWriteDto, List<MultipartFile> multipartFiles) throws IOException;

    Long createAnswer(Long boardId, AnswerWriteDto answerWriteDto, List<MultipartFile> multipartFiles) throws IOException;

    Long updateQuestion(Long boardId, QuestionWriteDto questionWriteDto, List<MultipartFile> multipartFiles) throws IOException;

    Long updateAnswer(Long boardId, AnswerWriteDto answerWriteDto, List<MultipartFile> multipartFiles) throws IOException;

    boolean validateMember(Long boardId, String boardType);

    void deleteQuestion(Long boardId);

    void deleteAnswer(Long boardId);
}
