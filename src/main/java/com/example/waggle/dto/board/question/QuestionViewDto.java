package com.example.waggle.dto.board.question;

import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.dto.board.answer.AnswerViewDto;
import com.example.waggle.dto.board.comment.CommentViewDto;
import com.example.waggle.dto.board.reply.ReplyViewDto;
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
public class QuestionViewDto {

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
    private List<CommentViewDto> comments = new ArrayList<>();
    @Builder.Default
    private List<ReplyViewDto> replies = new ArrayList<>();
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
    @Builder.Default
    private List<AnswerViewDto> answers = new ArrayList<>();

    static public QuestionViewDto toDto(Question question) {
        return QuestionViewDto.builder()
                .id(question.getId())
                .content(question.getContent())
                .username(question.getMember().getUsername())
                .title(question.getTitle())
                .createDate(question.getCreatedDate())
                .hashtags(question.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(question.getMedias().stream()
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .comments(question.getComments().stream()
                        .map(c -> CommentViewDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }

    public void linkAnswerView(List<AnswerViewDto> linkDtoList) {
        this.answers = linkDtoList;
    }

    public void linkRecommend(int count, boolean recommendIt) {
        this.recommendCount = count;
        this.recommendIt = recommendIt;
    }

    public QuestionViewDto(String username) {
        this.username = username;
    }
}
