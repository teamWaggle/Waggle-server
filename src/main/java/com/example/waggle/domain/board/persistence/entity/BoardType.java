package com.example.waggle.domain.board.persistence.entity;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.ErrorStatus;
import jakarta.persistence.DiscriminatorValue;
import lombok.Getter;

@Getter
public enum BoardType {
    STORY("type_story"),
    QUESTION("type_question"),
    SIREN("type_siren"),
    SCHEDULE("type_schedule");

    private final String discriminatorValue;

    BoardType(String discriminatorValue) {
        this.discriminatorValue = discriminatorValue;
    }

    public static BoardType fromDiscriminatorValue(Board board) {
        String value = board.getClass().getAnnotation(DiscriminatorValue.class).value();
        for (BoardType type : values()) {
            if (type.getDiscriminatorValue().equals(value)) return type;
        }
        throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
    }
}
