package com.recruit.assignment.common.exception;

import com.recruit.assignment.presentation.api.dto.response.ResponseCode;
import lombok.Getter;

public abstract class AbstractRuntimeException extends RuntimeException {
    @Getter
    protected final String message;

    protected AbstractRuntimeException(String message) {
        super(message);
        this.message = message;
    }

    protected AbstractRuntimeException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }

    protected AbstractRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public abstract ResponseCode getErrorCode();
}
