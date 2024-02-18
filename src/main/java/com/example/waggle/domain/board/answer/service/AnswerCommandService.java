package com.example.waggle.domain.board.answer.service;

import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerRequest.Post answerWriteDto,
                      List<MultipartFile> multipartFiles);

    Long createAnswerByUsername(Long questionId,
                                AnswerRequest.Post answerWriteDto,
                                List<MultipartFile> multipartFiles,
                                String username);

    Long updateAnswer(Long boardId,
                      AnswerRequest.Post answerWriteDto,
                      List<MultipartFile> multipartFiles,
                      List<String> deleteFiles);

    Long updateAnswerV2(Long boardId,
                        AnswerRequest.Post request,
                        MediaRequest.Put mediaUpdateDto,
                        List<MultipartFile> multipartFiles);

    Long updateAnswerByUsername(Long boardId,
                                String username,
                                AnswerRequest.Post request,
                                MediaRequest.Put mediaUpdateDto,
                                List<MultipartFile> multipartFiles);

    void deleteAnswer(Long boardId);

    void deleteAnswerByUsername(Long boardId, String username);
}
