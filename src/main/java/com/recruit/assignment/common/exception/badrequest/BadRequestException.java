package com.recruit.assignment.common.exception.badrequest;

import com.recruit.assignment.common.exception.AbstractRuntimeException;
import com.recruit.assignment.presentation.api.dto.response.ResponseCode;

/**
 * 잘못된 변수 요청
 */
public class BadRequestException extends AbstractRuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseCode getErrorCode() {
        return ResponseCode.BAD_REQUEST;
    }
}
