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
public class StoryDto {

    private Long id;
    private String content;
    private String username;
    private int recommend;
    private String thumbnail;
    private LocalDateTime createDate;

    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
    @Builder.Default
    private List<String> medias = new ArrayList<>();
    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();

    static public StoryDto toDto(Story story) {
        return StoryDto.builder()
                .id(story.getId())
                .content(story.getContent())
                .username(story.getMember().getUsername())
                .recommend(story.getRecommend())
                .thumbnail(story.getThumbnail())
                .createDate(story.getCreatedDate())
                .hashtags(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getH_content()).collect(Collectors.toList()))
                .medias(story.getMedias().stream()
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .comments(story.getComments().stream()
                        .map(c -> CommentDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }
}
