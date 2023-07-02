package ru.isis_test.core.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
    DistrictNotFound(1002, "District not found", HttpStatus.NOT_FOUND),
    FarmerNotFound(1000, "Farmer not found", HttpStatus.NOT_FOUND),
    OpfNotFound(1001, "Opf NotFound", HttpStatus.NOT_FOUND),
    InnRequired(1003, "Inn Required and must be 10 digits", HttpStatus.BAD_REQUEST),
    DigitsRequired(1004, "Only digits Required", HttpStatus.BAD_REQUEST),
    ValidationFailed(1005, "Validation failed", HttpStatus.BAD_REQUEST);
    private final int value;

    private final String error;

    private final HttpStatus status;

    ErrorCode(int value, String error, HttpStatus status) {
        this.value = value;
        this.status = status;
        this.error = error;
    }

    ErrorCode(int value) {
        this.value = value;
        this.error = String.valueOf(HttpStatus.BAD_REQUEST);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
