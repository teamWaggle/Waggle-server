package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.media.MediaRequest.MediaRequestDto;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.story.StoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryCommandService {

    Long createStory(StoryRequest createStoryRequest, List<MultipartFile> multipartFiles, Member member);

    Long updateStory(Long boardId,
                     StoryRequest updateStoryRequest,
                     MediaUpdateDto updateMediaRequest,
                     List<MultipartFile> multipartFiles,
                     Member member);

    Long updateStory(Long boardId,
                     StoryRequest updateStoryRequest,
                     MediaRequestDto updateMediaRequest,
                     Member member);

    void deleteStory(Long boardId, Member member);

}
