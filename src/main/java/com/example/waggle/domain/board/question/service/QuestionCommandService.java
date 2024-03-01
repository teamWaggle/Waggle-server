package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.media.MediaRequest;
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

    void deleteQuestion(Long boardId, Member member);

}
