package com.example.waggle.domain.board.answer.service;

import com.example.waggle.web.dto.answer.AnswerRequest.AnswerCreateDto;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerCreateDto answerWriteDto,
                      List<MultipartFile> multipartFiles);

    Long createAnswerByUsername(Long questionId,
                                AnswerCreateDto answerWriteDto,
                                List<MultipartFile> multipartFiles,
                                String username);

    Long updateAnswer(Long boardId,
                      AnswerCreateDto answerWriteDto,
                      List<MultipartFile> multipartFiles,
                      List<String> deleteFiles);

    Long updateAnswerV2(Long boardId,
                        AnswerCreateDto request,
                        MediaUpdateDto mediaUpdateDto,
                        List<MultipartFile> multipartFiles);

    Long updateAnswerByUsername(Long boardId,
                                String username,
                                AnswerCreateDto request,
                                MediaUpdateDto mediaUpdateDto,
                                List<MultipartFile> multipartFiles);

    void deleteAnswer(Long boardId);

    void deleteAnswerByUsername(Long boardId, String username);
}
