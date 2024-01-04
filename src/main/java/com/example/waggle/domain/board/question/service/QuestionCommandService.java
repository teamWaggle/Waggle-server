package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.question.QuestionRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest.Post request,
                        List<MultipartFile> multipartFiles)throws IOException;

    Long updateQuestion(Long boardId,
                        QuestionRequest.Post request,
                        List<MultipartFile> multipartFiles,
                        List<String> deleteFiles)throws IOException;


    void deleteQuestion(Long boardId);

}
