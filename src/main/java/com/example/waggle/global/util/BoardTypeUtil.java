package com.example.waggle.global.util;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.ErrorStatus;
import jakarta.persistence.DiscriminatorValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardTypeUtil {
    public static BoardType getBoardType(Board board) {
        String value = board.getClass().getAnnotation(DiscriminatorValue.class).value();
        log.info("description = {}", value);
        return switch (value) {
            case "type_story" -> BoardType.STORY;
            case "type_question" -> BoardType.QUESTION;
            case "type_siren" -> BoardType.SIREN;
            case "type_schedule" -> BoardType.SCHEDULE;
            default -> throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
        };
    }
}
