package com.example.waggle.web.dto.help;

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
    public class Post {
        private Long id;
        private String title;
        private String petName;
        private String petKind;
        private int petAge;
        private Gender petGender;
        private String contact;
        private String thumbnail;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String content;
        private String rfid;
        private String characteristic;
        private String username;
    }
}
