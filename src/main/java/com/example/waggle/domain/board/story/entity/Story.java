package com.example.waggle.domain.board.story.entity;


import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.member.entity.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_story")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends Board {
    private String thumbnail;

    public Story(Long id, Member member,
                 String content, List<BoardHashtag> boardHashtags,
                 List<Media> medias, String thumbnail) {
        super(id, member, content, boardHashtags, medias);
        this.thumbnail = thumbnail;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
