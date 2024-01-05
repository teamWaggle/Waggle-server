package com.example.waggle.web.dto.help;

import com.example.waggle.domain.board.help.entity.Help.Category;
import com.example.waggle.domain.member.entity.Gender;
import lombok.*;

import java.time.LocalDateTime;

public class HelpRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Post {

        private String title;
        private String petKind;
        private int petAge;
        private Gender petGender;
        private String contact;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String content;
        private Category category;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Put {
        private Long id;
        private String title;
        private String petKind;
        private int petAge;
        private Gender petGender;
        private String contact;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String content;
        private String username;
        private Category category;
    }
}
