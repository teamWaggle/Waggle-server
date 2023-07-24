package com.example.waggle.dto.board;

import com.example.waggle.domain.board.boardType.Question;
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
public class QuestionDto {

    private Long id;
    private String content;
    private String username;
    private String title;
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

    static public QuestionDto toDto(Question question, int count, boolean recommendIt) {
        return QuestionDto.builder()
                .id(question.getId())
                .content(question.getContent())
                .username(question.getMember().getUsername())
                .title(question.getTitle())
                .createDate(question.getCreatedDate())
                .recommendCount(count)
                .recommendIt(recommendIt)
                .hashtags(question.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(question.getMedias().stream()
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .comments(question.getComments().stream()
                        .map(c -> CommentDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }

    public Question toEntity(Member member) {
        return Question.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

    public QuestionDto(String username) {
        this.username = username;
    }
}
