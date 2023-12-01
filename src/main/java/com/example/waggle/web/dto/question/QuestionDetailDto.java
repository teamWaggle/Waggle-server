package com.example.waggle.web.dto.question;

import com.example.waggle.domain.board.question.domain.Question;
import com.example.waggle.web.dto.answer.AnswerDetailDto;
import com.example.waggle.web.dto.comment.CommentViewDto;
import com.example.waggle.web.dto.reply.ReplyViewDto;
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
public class QuestionDetailDto {

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
    private List<AnswerDetailDto> answers = new ArrayList<>();

    static public QuestionDetailDto toDto(Question question) {
        return QuestionDetailDto.builder()
                .id(question.getId())
                .content(question.getContent())
                .username(question.getMember().getUsername())
                .title(question.getTitle())
                .createDate(question.getCreatedDate())
                .hashtags(question.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(question.getMedias().stream()
                        .map(m -> m.getUploadFile().getStoreFileName()).collect(Collectors.toList()))
                .comments(question.getComments().stream()
                        .map(c -> CommentViewDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }

    public void linkAnswerView(List<AnswerDetailDto> linkDtoList) {
        this.answers = linkDtoList;
    }

    public void linkRecommend(int count, boolean recommendIt) {
        this.recommendCount = count;
        this.recommendIt = recommendIt;
    }

    public QuestionDetailDto(String username) {
        this.username = username;
    }
}
