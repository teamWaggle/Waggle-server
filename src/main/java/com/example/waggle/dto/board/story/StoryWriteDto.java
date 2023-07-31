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
@ToString(of = {"content"})
public class StoryWriteDto {

    private String content;
    private String thumbnail;

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
}
