package com.example.waggle.domain.board.answer.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerRequest.Post request,
                      List<MultipartFile> multipartFiles);

    Long createAnswer(Long questionId,
                      Member member,
                      AnswerRequest.Post request,
                      List<MultipartFile> multipartFiles);

    Long updateAnswer(Long boardId,
                      AnswerRequest.Post request,
                      List<MultipartFile> multipartFiles,
                      List<String> deleteFiles);

    Long updateAnswerV2(Long boardId,
                        AnswerRequest.Post request,
                        MediaRequest.Put mediaUpdateDto,
                        List<MultipartFile> multipartFiles);

    Long updateAnswer(Long boardId,
                      Member member,
                      AnswerRequest.Post request,
                      MediaRequest.Put mediaUpdateDto,
                      List<MultipartFile> multipartFiles);

    void deleteAnswer(Long boardId);

    void deleteAnswer(Long boardId, Member member);
}
