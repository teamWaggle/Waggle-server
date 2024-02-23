package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryCommandService {

    Long createStory(StoryRequest.Post request, List<MultipartFile> multipartFiles);

    Long createStory(Member member, StoryRequest.Post request, List<MultipartFile> multipartFiles);

    Long updateStory(Long boardId,
                     StoryRequest.Post storyWriteDto,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles);

    Long updateStoryV2(Long boardId,
                       StoryRequest.Post storyWriteDto,
                       MediaRequest.Put mediaListDto,
                       List<MultipartFile> multipartFiles);

    Long updateStory(Long boardId,
                     Member member,
                     StoryRequest.Post storyWriteDto,
                     MediaRequest.Put mediaListDto,
                     List<MultipartFile> multipartFiles);

    void deleteStory(Long boardId);

    void deleteStory(Long boardId, Member member);
}
