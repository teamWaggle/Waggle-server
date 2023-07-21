package com.example.waggle.domain.board.boardType;


import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.hashtag.BoardHashtag;


import com.example.waggle.domain.member.Member;
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
                 List<Media> medias, List<Comment> comments, String thumbnail) {
        super(id, member, content, boardHashtags, medias, comments);
        this.thumbnail = thumbnail;
    }

    public void changeStory(String content, String thumbnail) {
        this.content = content;
        this.thumbnail = thumbnail;
    }

}
