package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.question.QuestionRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest.Post request,
                        List<MultipartFile> multipartFiles);

    Long createQuestionByUsername(QuestionRequest.Post request,
                                  List<MultipartFile> multipartFiles,
                                  String username);

    Long updateQuestion(Long boardId,
                        QuestionRequest.Post request,
                        List<MultipartFile> multipartFiles,
                        List<String> deleteFiles);

    Long updateQuestionV2(Long boardId,
                          QuestionRequest.Post request,
                          MediaRequest.Put mediaUpdateDto,
                          List<MultipartFile> multipartFiles);

    Long updateQuestionByUsername(Long boardId,
                                  String username,
                                  QuestionRequest.Post request,
                                  MediaRequest.Put mediaUpdateDto,
                                  List<MultipartFile> multipartFiles);

    void deleteQuestion(Long boardId);

    void deleteQuestionByUsername(Long boardId, String username);

}
