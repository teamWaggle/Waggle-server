package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.story.StoryRequest;


public interface StoryCommandService {

    Long createStory(StoryRequest createStoryRequest, Member member);

    Long updateStory(Long boardId,
                     StoryRequest updateStoryRequest,
                     Member member);

    void deleteStory(Long boardId, Member member);

}
