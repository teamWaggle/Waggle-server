package com.example.waggle.web.converter;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.story.StoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class StoryConverter {

    public static StoryResponse.SummaryDto toSummaryDto(Story story) {
        return StoryResponse.SummaryDto.builder()
                .id(story.getId())
                .thumbnail(MediaUtil.getThumbnail(story))
                .build();
    }

    public static StoryResponse.ListDto toListDto(Page<Story> storyPage) {
        List<StoryResponse.SummaryDto> stories = storyPage.stream()
                .map(StoryConverter::toSummaryDto).collect(Collectors.toList());
        return StoryResponse.ListDto.builder()
                .storyList(stories)
                .totalStories(storyPage.getTotalElements())
                .isFirst(storyPage.isFirst())
                .isLast(storyPage.isLast())
                .build();
    }

    public static StoryResponse.DetailDto toDetailDto(Story story) {
        return StoryResponse.DetailDto.builder()
                .id(story.getId())
                .content(story.getContent())
                .createdDate(story.getCreatedDate())
                .hashtags(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getContent()).collect(Collectors.toList()))
                .medias(MediaUtil.getBoardMedias(story))
                .member(MemberConverter.toMemberSummaryDto(story.getMember()))
                .isMine(story.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
                .build();
    }
}
