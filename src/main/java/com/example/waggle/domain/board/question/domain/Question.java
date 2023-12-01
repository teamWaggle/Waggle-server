package com.example.waggle.domain.board.question.domain;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.hashtag.domain.BoardHashtag;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.domain.media.domain.Media;
import com.example.waggle.domain.comment.domain.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@ToString
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

    public Question(Long id, Member member,
                    String content, String title, List<BoardHashtag> boardHashtags,
                    List<Media> medias, List<Comment> comments, List<Answer> answers) {
        super(id, member, content, boardHashtags,medias, comments);
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

    public void changeQuestion(String content, String title) {
        this.content = content;
        this.title = title;
    }
}
