package com.example.waggle.domain.board.answer.service;

import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerRequest createAnswerRequest,
                      List<MultipartFile> multipartFiles);

    Long createAnswerByUsername(Long questionId,
                                AnswerRequest createAnswerRequest,
                                List<MultipartFile> multipartFiles,
                                String username);

    Long updateAnswer(Long boardId,
                      AnswerRequest updateAnswerRequest,
                      List<MultipartFile> multipartFiles,
                      List<String> deleteFiles);

    Long updateAnswerV2(Long boardId,
                        AnswerRequest updateAnswerRequest,
                        MediaUpdateDto updateMediaRequest,
                        List<MultipartFile> multipartFiles);

    Long updateAnswerByUsername(Long boardId,
                                String username,
                                AnswerRequest updateAnswerRequest,
                                MediaUpdateDto updateMediaRequest,
                                List<MultipartFile> multipartFiles);

    void deleteAnswer(Long boardId);

    void deleteAnswerByUsername(Long boardId, String username);
}
