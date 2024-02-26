package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.question.QuestionRequest.QuestionCreateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionCommandService {

    Long createQuestion(QuestionCreateDto request,
                        List<MultipartFile> multipartFiles);

    Long createQuestionByUsername(QuestionCreateDto request,
                                  List<MultipartFile> multipartFiles,
                                  String username);

    Long updateQuestion(Long boardId,
                        QuestionCreateDto request,
                        List<MultipartFile> multipartFiles,
                        List<String> deleteFiles);

    Long updateQuestionV2(Long boardId,
                          QuestionCreateDto request,
                          MediaUpdateDto mediaUpdateDto,
                          List<MultipartFile> multipartFiles);

    Long updateQuestionByUsername(Long boardId,
                                  String username,
                                  QuestionCreateDto request,
                                  MediaUpdateDto mediaUpdateDto,
                                  List<MultipartFile> multipartFiles);

    void deleteQuestion(Long boardId);

    void deleteQuestionByUsername(Long boardId, String username);

}
