package com.example.waggle.dto.board.story;

import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.comment.CommentViewDto;
import com.example.waggle.dto.validation.UpdateCheck;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
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


    //스토리 하나를 저장할 때 사용
    //스토리 안의 다른 엔티티는 개별로 저장, 하지만 메서드를 통해 연관관계를 맺어준다.
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
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .build();
    }

    public StoryWriteDto(String username) {
        this.username = username;
    }
}