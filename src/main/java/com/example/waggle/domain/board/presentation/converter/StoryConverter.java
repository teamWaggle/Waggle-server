package com.example.waggle.domain.board.presentation.converter;

import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StoryResponse.StoryDetailDto;
import com.example.waggle.domain.board.presentation.dto.story.StoryResponse.StorySummaryDto;
import com.example.waggle.domain.board.presentation.dto.story.StoryResponse.StorySummaryListDto;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.PageUtil;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class StoryConverter {

    public static StorySummaryDto toSummaryDto(Story story) {
        return StorySummaryDto.builder()
                .boardId(story.getId())
                .thumbnail(MediaUtil.getThumbnail(story))
                .build();
    }

    public static StorySummaryListDto toListDto(Page<Story> storyPage) {
        List<StorySummaryDto> stories = storyPage.stream()
                .map(StoryConverter::toSummaryDto).collect(Collectors.toList());
        return StorySummaryListDto.builder()
                .storyList(stories)
                .nextPageParam(PageUtil.countNextPage(storyPage))
                .build();
    }

    public static StoryDetailDto toDetailDto(Story story) {
        return StoryDetailDto.builder()
                .boardId(story.getId())
                .content(story.getContent())
                .createdDate(story.getCreatedDate())
                .hashtagList(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getContent()).collect(Collectors.toList()))
                .mediaList(MediaUtil.getBoardMedias(story))
                .member(MemberConverter.toMemberSummaryDto(story.getMember()))
                .build();
    }
}
