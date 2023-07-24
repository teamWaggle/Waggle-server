package com.example.waggle.dto.board;

import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.member.Member;
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
public class AnswerDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createDate;
    private int recommendCount;
    private boolean recommendIt;
    
    @Builder.Default
    private List<String> medias = new ArrayList<>();
    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();
    @Builder.Default
    private List<ReplyDto> replies = new ArrayList<>();
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    static public AnswerDto toDto(Answer answer, int count, boolean recommendIt) {
        return AnswerDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .username(answer.getMember().getUsername())
                .createDate(answer.getCreatedDate())
                .recommendCount(count)
                .recommendIt(recommendIt)
                .hashtags(answer.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(answer.getMedias().stream()
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .comments(answer.getComments().stream()
                        .map(c -> CommentDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }

    public Answer toEntity(Member member) {
        return Answer.builder()
                .content(content)
                .member(member)
                .build();
    }
}
