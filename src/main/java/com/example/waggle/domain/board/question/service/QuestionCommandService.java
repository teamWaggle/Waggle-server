package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest createQuestionRequest,
                        List<MultipartFile> multipartFiles);

    Long createQuestionByUsername(QuestionRequest createQuestionRequest,
                                  List<MultipartFile> multipartFiles,
                                  String username);

    Long updateQuestion(Long boardId,
                        QuestionRequest updateQuestionRequest,
                        List<MultipartFile> multipartFiles,
                        List<String> deleteFiles);

    Long updateQuestionV2(Long boardId,
                          QuestionRequest updateQuestionRequest,
                          MediaUpdateDto updateMediaRequest,
                          List<MultipartFile> multipartFiles);

    Long updateQuestionByUsername(Long boardId,
                                  String username,
                                  QuestionRequest updateQuestionRequest,
                                  MediaUpdateDto updateMediaRequest,
                                  List<MultipartFile> multipartFiles);

    void deleteQuestion(Long boardId);

    void deleteQuestionByUsername(Long boardId, String username);

}
