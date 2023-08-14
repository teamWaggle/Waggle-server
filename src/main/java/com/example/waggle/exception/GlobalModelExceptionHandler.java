package com.example.waggle.exception;

import com.example.waggle.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalModelExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ModelAndView handleException(CustomException e) {
        //view path
        ModelAndView mv = new ModelAndView("error");
        ErrorDto errorDto = errorToDto(e.getErrorCode());
        mv.addObject("errorMessage", errorDto);
        return mv;
    }

    private ErrorDto errorToDto(ErrorCode e) {
        return ErrorDto.builder()
                .error(e.getHttpStatus().name())
                .message(e.getDetail())
                .status(e.getHttpStatus().value())
                .build();
    }
}
