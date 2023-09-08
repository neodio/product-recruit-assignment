package com.recruit.assignment.common.exception.badrequest;

import com.recruit.assignment.presentation.api.dto.response.ResponseCode;

/**
 * 잘못된 변수 요청
 */
public class TypeMismatchException extends BadRequestException {
    public TypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeMismatchException(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseCode getErrorCode() {
        return ResponseCode.BAD_REQUEST_TYPE_MISMATCH;
    }
}
