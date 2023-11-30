package com.example.waggle.domain.board.story.dto;

import com.example.waggle.domain.board.story.domain.Story;
import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorySummaryDto {

    private Long id;
    private String username;
    private UploadFile profileImg;
    private String thumbnail;
    private String createdDate;
    private int recommendCount;
    private boolean recommendIt;
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    static public StorySummaryDto toDto(Story story) {
        return StorySummaryDto.builder()
                .id(story.getId())
                .username(story.getMember().getUsername())
                .profileImg(story.getMember().getProfileImg())
                .thumbnail(story.getThumbnail())
                .createdDate(DateUtil.simpleStoryTimeFormat(story.getCreatedDate()))
                .hashtags(story.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getTag()).collect(Collectors.toList()))
                .build();
    }

    public void linkRecommend(int count, boolean recommendIt) {
        this.recommendCount = count;
        this.recommendIt = recommendIt;
    }
}
