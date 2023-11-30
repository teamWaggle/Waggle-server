package com.example.waggle.global.exception;

import com.example.waggle.global.dto.error.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalModelExceptionHandler {

    @ExceptionHandler(CustomPageException.class)
    public ModelAndView handleException(HttpServletRequest request, CustomPageException e) {
        //view path
        ModelAndView mv = new ModelAndView("error/error_page");
        log.info("path is {}", mv.getViewName());
        ErrorDto errorDto = errorToDto(e.getErrorCode());
        mv.addObject("errorMessage", errorDto);
        mv.addObject("redirectURL", request.getRequestURI());
        return mv;
    }
    @ExceptionHandler(CustomAlertException.class)
    public ModelAndView handleException(CustomAlertException e) {
        //view path
        ModelAndView mv = new ModelAndView("error/error_alert");
        log.info("path is {}", mv.getViewName());
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
