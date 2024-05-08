package com.example.waggle.domain.board.persistence.entity;


import com.example.waggle.domain.hashtag.persistence.entity.BoardHashtag;
import com.example.waggle.domain.media.persistence.entity.Media;
import com.example.waggle.domain.member.persistence.entity.Member;
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

    public Story(Long id, Member member,
                 String content, List<BoardHashtag> boardHashtags,
                 List<Media> medias, String thumbnail) {
        super(id, member, content, boardHashtags, medias);
    }

    public void changeContent(String content) {
        this.content = content;
    }


}
