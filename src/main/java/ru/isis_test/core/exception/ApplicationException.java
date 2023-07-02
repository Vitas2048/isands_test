package ru.isis_test.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.isis_test.core.message.ErrorCode;

import java.util.function.Supplier;

@Getter@Setter
@AllArgsConstructor
public class ApplicationException extends Throwable implements Supplier<ApplicationException> {
    private ErrorCode code;

    @Override
    public ApplicationException get() {
        return new ApplicationException(code);
    }
}
