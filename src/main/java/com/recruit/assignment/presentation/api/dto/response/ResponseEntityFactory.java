package com.recruit.assignment.presentation.api.dto.response;

import com.recruit.assignment.common.exception.AbstractRuntimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseEntityFactory {
    private static class ErrorResponseEntityFactoryHolder {
        private static final ResponseEntityFactory INSTANCE = new ResponseEntityFactory();
    }

    public static ResponseEntityFactory getInstance() {
        return ErrorResponseEntityFactoryHolder.INSTANCE;
    }

    public static ResponseEntity<CustomResponse<Void>> createNoneDataResponse(ResponseCode responseCode) {
        return getInstance().internalCreate(
                responseCode.getStatus(),
                CustomResponse.createResponse(responseCode)
        );
    }

    public static <T> ResponseEntity<CustomResponse<T>> createResponse(ResponseCode responseCode, T data) {
        return getInstance().internalCreate(
                responseCode.getStatus(),
                CustomResponse.createResponse(responseCode, data)
        );
    }

    public static ResponseEntity<CustomResponse<Void>> createErrorResponse(AbstractRuntimeException ex) {
        ResponseCode responseCode = ex.getErrorCode();
        log.error("SESSION ID=" + MDC.getMDCAdapter().get("REQUEST_ID")
                + " ERROR CODE=" + "'" + responseCode.getCode() + "'"
                + " ERROR DESC=" + "'" + responseCode.getDescription() + "'"
                + " ERROR MESSAGE=" + "'" + ex.getMessage() + "'", ex);

        return getInstance().internalCreate(
                responseCode.getStatus(),
                CustomResponse.createResponse(responseCode, ex.getMessage())
        );
    }

    private <T> ResponseEntity<CustomResponse<T>> internalCreate(int status, CustomResponse<T> customResponse) {
        return ResponseEntity
                .status(status)
                .body(customResponse);
    }
}
