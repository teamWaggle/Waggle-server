package com.example.waggle.domain.board.answer.service;

import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerRequest.Post answerWriteDto,
                      List<MultipartFile> multipartFiles) throws IOException;

    Long createAnswerByUsername(Long questionId,
                                AnswerRequest.Post answerWriteDto,
                                List<MultipartFile> multipartFiles,
                                String username) throws IOException;

    Long updateAnswer(Long boardId,
                      AnswerRequest.Post answerWriteDto,
                      List<MultipartFile> multipartFiles,
                      List<String> deleteFiles) throws IOException;

    Long updateAnswerV2(Long boardId,
                        AnswerRequest.Put answerUpdateDto,
                        MediaRequest.Put mediaUpdateDto,
                        List<MultipartFile> multipartFiles) throws IOException;

    void deleteAnswer(Long boardId);


}
