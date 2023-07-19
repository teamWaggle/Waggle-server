package com.example.waggle.domain.board.hashtag;

import com.example.waggle.component.BaseEntity;
import com.example.waggle.component.BaseTimeEntity;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hashtag extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;


    private String tag;
/**
 * hashtag내에서 boardHahstag를 컬렉션으로 가질지 에 대해 고민을 많이 해봣는데
 * 아무래도 빼는게 괜찮을 것 같다.
 * hahstag를 통한 board 조회는 쿼리를 통해 하고
 * hashtag데이터 자체의 조회는 자유롭게 사용할 수 있는게 좋을 것 같다.
 */
//    @OneToMany(mappedBy = "board")
//    @Builder.Default
//    private List<BoardHashtag> boardHashtags = new ArrayList<>();

}
