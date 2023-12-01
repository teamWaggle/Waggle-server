package com.example.waggle.web.dto.answer;

import com.example.waggle.domain.board.question.entity.Answer;
import com.example.waggle.domain.member.domain.Member;
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
public class AnswerDetailDto {

    private Long id;
    private String content;
    private String username;
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

    static public AnswerDetailDto toDto(Answer answer) {
        return AnswerDetailDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .username(answer.getMember().getUsername())
                .createDate(answer.getCreatedDate())
                .hashtags(answer.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .medias(answer.getMedias().stream()
                        .map(m -> m.getUploadFile().getStoreFileName()).collect(Collectors.toList()))
                .comments(answer.getComments().stream()
                        .map(c -> CommentViewDto.toDto(c)).collect(Collectors.toList()))
                .build();
    }

    public Answer toEntity(Member member) {
        return Answer.builder()
                .content(content)
                .member(member)
                .build();
    }

    public void linkRecommend(int count, boolean recommendIt) {
        this.recommendCount = count;
        this.recommendIt = recommendIt;
    }
}
