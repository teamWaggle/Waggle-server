package com.example.waggle.domain.board.help.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.help.HelpRequest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "help")
@SuperBuilder
@DiscriminatorValue("type_help")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Help extends Board {

    private String title;

    private String petName;
    private String petKind;
    private int petAge;

    @Enumerated
    private Gender petGender;

    private LocalDateTime lostDate;
    private String contact;
    private String thumbnail;

    public Help(Long id, Member member,
                String content, List<BoardHashtag> boardHashtags,
                List<Media> medias, List<Comment> comments,
                String title, String petName, String petKind,
                int petAge, Gender petGender,
                LocalDateTime lostDate, String contact, String thumbnail) {
        super(id, member, content, boardHashtags, medias, comments);
        this.title = title;
        this.petName = petName;
        this.petKind = petKind;
        this.petAge = petAge;
        this.petGender = petGender;
        this.lostDate = lostDate;
        this.contact = contact;
        this.thumbnail = thumbnail;
    }

    public void changeHelp(HelpRequest.Post helpUWriteDto) {
        this.title = helpUWriteDto.getTitle();
        this.petName = helpUWriteDto.getPetName();
        this.petKind = helpUWriteDto.getPetKind();
        this.petAge = helpUWriteDto.getPetAge();
        this.petGender = helpUWriteDto.getPetGender();
        this.lostDate = helpUWriteDto.getLostDate();
        this.contact = helpUWriteDto.getContact();
        //this.thumbnail = helpUWriteDto.getThumbnail();
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
