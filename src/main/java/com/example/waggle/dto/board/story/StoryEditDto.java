package com.example.waggle.dto.board.story;

import com.example.waggle.domain.board.boardType.Story;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"id", "content"})
public class StoryEditDto {

    private Long id;
    private String content;
    private String thumbnail;

    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
    @Builder.Default
    private List<String> medias = new ArrayList<>();

    static public StoryEditDto toDto(Story story) {
        return StoryEditDto.builder()
                .id(story.getId())
                .content(story.getContent())
                .thumbnail(story.getThumbnail())
                .hashtags(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(story.getMedias().stream()
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .build();
    }

}
