package com.recruit.assignment.presentation.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    public static CustomResponse<Void> createResponse(ResponseCode responseCode) {
        return new CustomResponse<>(responseCode.getCode(), responseCode.getDescription(), null);
    }

    public static CustomResponse<Void> createResponse(ResponseCode responseCode, String message) {
        return new CustomResponse<>(responseCode.getCode(), message, null);
    }

    public static <T> CustomResponse<T> createResponse(ResponseCode responseCode, T data) {
        return new CustomResponse<>(responseCode.getCode(), responseCode.getDescription(), data);
    }

    public static <T> CustomResponse<T> createResponse(ResponseCode responseCode, String message, T data) {
        return new CustomResponse<>(responseCode.getCode(), message, data);
    }
}
