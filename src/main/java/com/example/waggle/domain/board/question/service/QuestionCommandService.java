package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest questionCreateRequest,
                        List<MultipartFile> multipartFiles);

    Long createQuestionByUsername(QuestionRequest questionCreateRequest,
                                  List<MultipartFile> multipartFiles,
                                  String username);

    Long updateQuestion(Long boardId,
                        QuestionRequest questionUpdateRequest,
                        List<MultipartFile> multipartFiles,
                        List<String> deleteFiles);

    Long updateQuestionV2(Long boardId,
                          QuestionRequest questionUpdateRequest,
                          MediaUpdateDto mediaUpdateRequest,
                          List<MultipartFile> multipartFiles);

    Long updateQuestionByUsername(Long boardId,
                                  String username,
                                  QuestionRequest questionUpdateRequest,
                                  MediaUpdateDto mediaUpdateRequest,
                                  List<MultipartFile> multipartFiles);

    void deleteQuestion(Long boardId);

    void deleteQuestionByUsername(Long boardId, String username);

}
