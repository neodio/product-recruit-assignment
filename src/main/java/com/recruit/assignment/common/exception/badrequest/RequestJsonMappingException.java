package com.recruit.assignment.common.exception.badrequest;

import com.recruit.assignment.presentation.api.dto.response.ResponseCode;

/**
 * 잘못된 변수 요청
 */
public class RequestJsonMappingException extends BadRequestException {
    public RequestJsonMappingException(String message) {
        super(message);
    }

    @Override
    public ResponseCode getErrorCode() {
        return ResponseCode.BAD_REQUEST_JSON_MAPPING;
    }
}
