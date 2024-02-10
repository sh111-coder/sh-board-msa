package com.example.boardservice.board.exception;

import com.example.shboardcommon.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class BoardExceptionHandler {

    @ExceptionHandler(value = {
            BoardException.NotFoundBoardException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomNotFoundException(final RuntimeException exception) {
        final String errorMessage = exception.getMessage();
        log.warn(errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(value = {
            NoFallbackAvailableException.class
    })
    public ResponseEntity<ErrorResponse> handleFailOpenFeignException(final NoFallbackAvailableException exception) {
        final String errorMessage = exception.getMessage();
        log.warn(errorMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErrorResponse(errorMessage));
    }
}
