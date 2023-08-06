package com.example.waggle.dto.board.story;

import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.comment.CommentViewDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StoryViewDto {

    private Long id;
    private String content;
    private String username;
    private String thumbnail;
    private LocalDateTime createDate;
    private int recommendCount;
    private boolean recommendIt;

    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
    @Builder.Default
    private List<String> medias = new ArrayList<>();
    @Builder.Default
    private List<CommentViewDto> comments = new ArrayList<>();

    //스토리 하나를 조회할 때 사용
    static public StoryViewDto toDto(Story story) {
        return StoryViewDto.builder()
                .id(story.getId())
                .content(story.getContent())
                .username(story.getMember().getUsername())
                .thumbnail(story.getThumbnail())
                .createDate(story.getCreatedDate())
                .hashtags(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(story.getMedias().stream()
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .comments(story.getComments().stream()
                        .map(c -> CommentViewDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }

    public void linkRecommend(int count, boolean recommendIt) {
        this.recommendCount = count;
        this.recommendIt = recommendIt;
    }


    public StoryViewDto(String username) {
        this.username = username;
    }
}
