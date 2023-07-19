package com.example.waggle.domain.board;

import com.example.waggle.domain.Like;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;

import com.example.waggle.domain.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_story")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends Board {
    private String thumbnail;


    public Story(Long id, Member member,
                 String content, List<Like> likes, List<BoardHashtag> boardHashtags,
                 List<Media> medias, List<Comment> comments, String thumbnail) {
        super(id, member, content, likes, boardHashtags, medias, comments);
        this.thumbnail = thumbnail;
    }

    public void changeStory(String content, String thumbnail) {
        this.content = content;
        this.thumbnail = thumbnail;
    }

}
