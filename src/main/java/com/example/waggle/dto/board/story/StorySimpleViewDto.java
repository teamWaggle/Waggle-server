package com.example.waggle.dto.board.story;

import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.utils.DateUtil;
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
public class StorySimpleViewDto {

    private Long storyId;
    private String username;
    private UploadFile profileImg;
    private String thumbnail;
    private String createdDate;
    private int recommendCount;
    private boolean recommendIt;
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    static public StorySimpleViewDto toDto(Story story, int count, boolean recommendIt ) {
        return StorySimpleViewDto.builder()
                .storyId(story.getId())
                .username(story.getMember().getUsername())
                .profileImg(story.getMember().getProfileImg())
                .thumbnail(story.getThumbnail())
                .createdDate(DateUtil.txtDate(story.getCreatedDate()))
                .recommendCount(count)
                .recommendIt(recommendIt)
                .hashtags(story.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getTag()).collect(Collectors.toList()))
                .build();

    }
}
