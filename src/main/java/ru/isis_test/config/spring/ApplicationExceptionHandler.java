package ru.isis_test.config.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.SemanticException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.core.exception.ValidationException;
import ru.isis_test.core.message.ErrorCode;
import ru.isis_test.core.message.ErrorResponse;


@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    private ResponseEntity<ErrorResponse> handle(ApplicationException e) {
        log.debug("Handle error code: {}", e.getCode().getValue());
        return ResponseEntity
                .status(e.getCode().getStatus())
                .body(new ErrorResponse(e.getCode(), e.getCode().getStatus().toString()));
    }

    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<ErrorResponse> handle(ValidationException e) {
        log.debug("Handle error code: {}", e.getMessage());
        int code = ErrorCode.DigitsRequired.getStatus().value();
        return ResponseEntity
                .status(ErrorCode.ValidationFailed.getStatus())
                .body(new ErrorResponse(ErrorCode.ValidationFailed, e.getMessage()));
    }

    @ExceptionHandler(SemanticException.class)
    private ResponseEntity<ErrorResponse> handle(SemanticException e) {
        log.debug("Handle error code: {}", e.getMessage());
        int code = ErrorCode.DigitsRequired.getStatus().value();
        return ResponseEntity
                .status(ErrorCode.ValidationFailed.getStatus())
                .body(new ErrorResponse(ErrorCode.ValidationFailed, e.getMessage()));
    }
}
