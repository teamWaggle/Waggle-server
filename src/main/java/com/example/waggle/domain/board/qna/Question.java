package com.example.waggle.domain.board.qna;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Board {
    private String title;

    @Builder.Default
    @OneToMany(mappedBy = "question",cascade = CascadeType.PERSIST,orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Question(Long id, Member member, LocalDateTime createdDate,
                 String content, String title, List<BoardHashtag> boardHashtags,
                 List<Media> medias, List<Comment> comments, List<Answer> answers) {
        super(id, member, createdDate, content,boardHashtags,medias, comments);
        this.title = title;
        if (answers != null) {
            for (Answer answer : answers) {
                addAnswer(answer);
            }
        }
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }

}
