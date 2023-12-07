package com.example.waggle.domain.board.question.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.member.entity.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ToString
@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Board {
    private String title;

    public Question(Long id, Member member,
                    String content, String title, List<BoardHashtag> boardHashtags,
                    List<Media> medias) {
        super(id, member, content, boardHashtags,medias);
        this.title = title;
    }

    public void changeQuestion(String content, String title) {
        this.content = content;
        this.title = title;
    }
}
