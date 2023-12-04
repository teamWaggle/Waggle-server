package com.example.waggle.global.exception;

import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.Reason;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity handleCustomException(GeneralException e, HttpServletRequest request) {
        Reason errorReasonHttpStatus = e.getErrorReasonHttpStatus();
        log.error("handleCustomException throw CustomException : {}", errorReasonHttpStatus);
        return handleExceptionInternal(e, errorReasonHttpStatus, null, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, Reason reason,
                                                           HttpHeaders headers, HttpServletRequest request) {

        ApiResponseDto<Object> body = ApiResponseDto.onFailure(reason.getCode(), reason.getMessage(), null);

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }

}
