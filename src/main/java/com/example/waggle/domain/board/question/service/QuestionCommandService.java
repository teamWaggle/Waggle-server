package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.media.MediaRequest.MediaRequestDto;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest createQuestionRequest,
                        List<MultipartFile> multipartFiles,
                        Member member);

    Long updateQuestion(Long boardId,
                        QuestionRequest updateQuestionRequest,
                        MediaUpdateDto updateMediaRequest,
                        List<MultipartFile> multipartFiles,
                        Member member);

    Long updateQuestion(Long boardId,
                        QuestionRequest updateQuestionRequest,
                        MediaRequestDto updateMediaRequest,
                        Member member);

    void convertStatus(Long boardId, Member member);

    void deleteQuestion(Long boardId, Member member);

    void increaseQuestionViewCount(Long boardId);

}
