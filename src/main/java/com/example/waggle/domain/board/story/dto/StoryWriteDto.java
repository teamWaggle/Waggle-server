package com.example.waggle.domain.board.story.dto;

import com.example.waggle.domain.board.story.domain.Story;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.global.validation.UpdateCheck;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"id", "content", "username"})
public class StoryWriteDto {
    @NotBlank(groups = UpdateCheck.class)
    private Long id;
    @Max(500)
    private String content;
    private String thumbnail;
    private String username;

    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
    @Builder.Default
    private List<String> medias = new ArrayList<>();

    public Story toEntity(Member member) {
        return Story.builder()
                .member(member)
                .content(content)
                .thumbnail(thumbnail)
                .build();
    }

    static public StoryWriteDto toDto(Story story) {
        return StoryWriteDto.builder()
                .id(story.getId())
                .content(story.getContent())
                .thumbnail(story.getThumbnail())
                .username(story.getMember().getUsername())
                .hashtags(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(story.getMedias().stream()
                        .map(m -> m.getUploadFile().getStoreFileName()).collect(Collectors.toList()))
                .build();
    }

    public StoryWriteDto(String username) {
        this.username = username;
    }
    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}