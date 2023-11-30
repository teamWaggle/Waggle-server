package com.example.waggle.domain.board.story.dto;

import com.example.waggle.domain.board.story.domain.Story;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.domain.comment.dto.CommentViewDto;
import com.example.waggle.commons.util.DateUtil;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StoryDetailDto {

    private Long id;
    private String content;
    private String username;
    private UploadFile profileImg;
    private String thumbnail;
    private String createdDate;
    private int recommendCount;
    private boolean recommendIt;

    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
    @Builder.Default
    private List<String> medias = new ArrayList<>();
    @Builder.Default
    private List<CommentViewDto> comments = new ArrayList<>();

    static public StoryDetailDto toDto(Story story) {
        return StoryDetailDto.builder()
                .id(story.getId())
                .content(story.getContent())
                .username(story.getMember().getUsername())
                .profileImg(story.getMember().getProfileImg())
                .thumbnail(story.getThumbnail())
                .createdDate(DateUtil.storyTimeFormat(story.getCreatedDate()))
                .hashtags(story.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(story.getMedias().stream()
                        .map(m -> m.getUploadFile().getStoreFileName()).collect(Collectors.toList()))
                .comments(story.getComments().stream()
                        .map(c -> CommentViewDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }

    public void linkRecommend(int count, boolean recommendIt) {
        this.recommendCount = count;
        this.recommendIt = recommendIt;
    }


    public StoryDetailDto(String username) {
        this.username = username;
    }
}
