package com.recruit.assignment.common.exception.system;

import com.recruit.assignment.common.exception.AbstractRuntimeException;
import com.recruit.assignment.presentation.api.dto.response.ResponseCode;

/**
 * 시스템 예외
 */
public class SystemException extends AbstractRuntimeException {
    public SystemException(Throwable cause) {
        super(cause);
    }

    @Override
    public ResponseCode getErrorCode() {
        return ResponseCode.INTERNAL_SERVER_ERROR;
    }
}
