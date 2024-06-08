package com.example.waggle.global.util;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.ErrorStatus;
import org.springframework.context.annotation.Description;

public class BoardTypeUtil {
    public static BoardType getBoardType(Board board) {
        switch (board.getClass().getAnnotation(Description.class).value()) {
            case "type_story" -> {
                return BoardType.STORY;
            }
            case "type_question" -> {
                return BoardType.QUESTION;
            }
            case "type_siren" -> {
                return BoardType.SIREN;
            }
            case "type_schedule" -> {
                return BoardType.SCHEDULE;
            }
            default -> {
                throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
            }
        }
    }
}
