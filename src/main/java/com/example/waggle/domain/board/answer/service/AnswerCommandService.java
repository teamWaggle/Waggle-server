package com.example.waggle.domain.board.answer.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.media.MediaRequest;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerRequest createAnswerRequest,
                      List<MultipartFile> multipartFiles,
                      Member member);

    Long updateAnswer(Long boardId,
                      AnswerRequest updateAnswerRequest,
                      MediaUpdateDto updateMediaRequest,
                      List<MultipartFile> multipartFiles,
                      Member member);

    void deleteAnswer(Long boardId, Member member);
}
