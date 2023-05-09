package com.example.waggle.domain.board.sos;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.Sex;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Sos extends Board {

    private String title;

    private String name;
    private String kind;
    private int age;
    @Enumerated
    private Sex sex;

    private String lostLocate;

    private LocalDateTime lostDate;

    private String RFID;

    private String contact;

}
