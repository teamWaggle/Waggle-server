package com.example.waggle.dto.board;

import com.example.waggle.domain.board.Story;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorySimpleDto {

    private Long storyId;
    private String thumbnail;
    private LocalDateTime createDate;
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    static public StorySimpleDto toDto(Story story) {
        return StorySimpleDto.builder()
                .storyId(story.getId())
                .thumbnail(story.getThumbnail())
                .createDate(story.getCreatedDate())
                .hashtags(story.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getTag()).collect(Collectors.toList()))
                .build();
    }
}
