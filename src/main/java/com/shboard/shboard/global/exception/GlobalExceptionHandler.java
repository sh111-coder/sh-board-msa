package com.shboard.shboard.global.exception;


import java.util.Random;

import com.shboard.shboard.board.exception.BoardException;
import com.shboard.shboard.global.auth.AuthException;
import com.shboard.shboard.member.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final String defaultErrorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        final Object rejectedValue = exception.getBindingResult().getFieldError().getRejectedValue();
        log.warn(String.format(defaultErrorMessage + " [입력된 값 : %s]", rejectedValue));

        return ResponseEntity.badRequest().body(new ErrorResponse(defaultErrorMessage));
    }

    @ExceptionHandler(value = {
            AuthException.FailAuthenticationMemberException.class
    })
    public ResponseEntity<ErrorResponse> handleUnAuthorizedException(final AuthException exception) {
        final String errorMessage = exception.getMessage();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(errorMessage));
    }

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
            MemberException.NotFoundMemberException.class,
            BoardException.NotFoundBoardException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomNotFoundException(final RuntimeException exception) {
        final String errorMessage = exception.getMessage();
        log.warn(errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException exception) {
        final String errorKey = generateRandomKey();
        log.error(String.format(exception + "error Key : [%s]", errorKey));

        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(String.format("관리자에게 문의하세요. error Key : [%s]", errorKey)));
    }

    private String generateRandomKey() {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int randomLength = 5;
        final StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int generateIdx = 0; generateIdx < randomLength; generateIdx++) {
            int randomIdx = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIdx);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
