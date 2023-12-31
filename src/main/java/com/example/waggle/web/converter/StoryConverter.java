package com.example.waggle.web.converter;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.global.util.DateUtil;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.story.StoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class StoryConverter {

    public static StoryResponse.SummaryDto toSummaryDto(Story story) {
        String thumbnail = null;
        if (!story.getMedias().isEmpty()) {
            thumbnail = story.getMedias().get(0).getUploadFile();
        }
        return StoryResponse.SummaryDto.builder()
                .id(story.getId())
                .username(story.getMember().getUsername())
                .profileImg(story.getMember().getProfileImgUrl())
                .createdDate(DateUtil.simpleStoryTimeFormat(story.getCreatedDate()))
                .thumbnail(thumbnail)
                .hashtags(story.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getContent()).collect(Collectors.toList()))
                .build();
    }

    public static StoryResponse.ListDto toListDto(Page<Story> storyPage) {
        List<StoryResponse.SummaryDto> stories = storyPage.stream()
                .map(StoryConverter::toSummaryDto).collect(Collectors.toList());
        return StoryResponse.ListDto.builder()
                .storyList(stories)
                .totalQuestions(storyPage.getTotalElements())
                .isFirst(storyPage.isFirst())
                .isLast(storyPage.isLast())
                .build();
    }

    public static StoryResponse.DetailDto toDetailDto(Story story) {
        return StoryResponse.DetailDto.builder()
                .id(story.getId())
                .content(story.getContent())
                .username(story.getMember().getUsername())
                .profileImg(story.getMember().getProfileImgUrl())
                .createdDate(DateUtil.storyTimeFormat(story.getCreatedDate()))
                .hashtags(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getContent()).collect(Collectors.toList()))
                .medias(story.getMedias().stream()
                        .map(media -> MediaUtil.appendUri(media.getUploadFile())).collect(Collectors.toList()))
                .build();
    }
}
