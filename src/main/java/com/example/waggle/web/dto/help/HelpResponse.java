package com.example.waggle.web.dto.help;

import com.example.waggle.domain.member.entity.Gender;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HelpResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public class SummaryDto {

        private Long id;
        private String title;
        private String petName;
        private String petKind;
        private int petAge;
        private Gender petGender;
        private String thumbnail;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String username;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public class DetailDto {
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

        @Builder.Default
        private List<String> medias = new ArrayList<>();
        private String username;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<HelpResponse.SummaryDto> helpList = new ArrayList<>();
        private long totalHelps;
        private boolean isFirst;
        private boolean isLast;
    }
}
