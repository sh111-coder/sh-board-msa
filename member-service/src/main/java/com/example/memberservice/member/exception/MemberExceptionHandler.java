package com.example.memberservice.member.exception;

import com.example.shboardcommon.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice

public class MemberExceptionHandler {

    @ExceptionHandler(value = {
            MemberException.WrongLengthLoginIdException.class,
            MemberException.WrongPatternPasswordException.class,
            MemberException.WrongLengthNicknameException.class,
            MemberException.FailLoginException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomBadRequestException(final RuntimeException exception) {
        final String errorMessage = exception.getMessage();
        log.warn(errorMessage);

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(value = {
            MemberException.NotFoundMemberException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomNotFoundException(final RuntimeException exception) {
        final String errorMessage = exception.getMessage();
        log.warn(errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ErrorResponse(errorMessage));
    }
}
