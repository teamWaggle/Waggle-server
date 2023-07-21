package com.example.waggle.domain.board.boardType;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.boardType.Question;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends Board {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;


    /**
     * builder를 따로 설정하지 않는 이유
     * 어차피 superBuilder어노테이션으로 인해
     * 자동적으로 필드들이 builder로 설정이 가능한데
     * 다른 boardEntity들과 달리
     * answer은 builder내에서 사용할 생성자 메서드나 연관관계 메서드가 없기 때문에
     * 다른 boardType 엔티티와 달리 따로 setting을 하지 않는다.
     */

    protected void setQuestion(Question question) {
        this.question = question;
    }

    public void changeAnswer(String content) {
        this.content = content;
    }
}
